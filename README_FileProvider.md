### FileProvider优化

*启动耗时优化*

```
plugins {
    id("FileProviderPlugin")
}
```
耗时点：
* FileProvider 初始化发生在 application.attachBaseContext 之后, application.onCreate 之前，其中 mStrategy 初始化较为耗时。

修改点：
* 通过asm修改字节码，在 FileProvider 初始化时设置 ProviderInfo.grantUriPermissions = false，使其不初始化 mStrategy。 同时修改为在使用 mStrategy 时判断是否为空，若为空再进行初始化。转移耗时到使用时。

修改后代码参考：

```
    @Override // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo info) {
        if (!info.exported) {
            if (!info.grantUriPermissions) {
                throw new SecurityException("Provider must grant uri permissions");
            }
            info.grantUriPermissions = false;
            try {
                super.attachInfo(context, info);
                if (info.exported) {
                    throw new SecurityException("Provider must not be exported");
                }
                if (!info.grantUriPermissions) {
                    throw new SecurityException("Provider must grant uri permissions");
                }
                String authority = info.authority.split(";")[0];
                HashMap<String, PathStrategy> hashMap = sCache;
                synchronized (hashMap) {
                    hashMap.remove(authority);
                }
                this.mStrategy = getPathStrategy(context, authority, this.mResourceId);
                return;
            } catch (Throwable th) {
                th.printStackTrace();
                this.authority = info.authority.split(";")[0];
                info.grantUriPermissions = true;
                return;
            }
        }
        throw new SecurityException("Provider must not be exported");
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (this.mStrategy == null) {
            this.mStrategy = getPathStrategy(getContext(), this.authority, this.mResourceId);
        }
        // ...
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        if (this.mStrategy == null) {
            this.mStrategy = getPathStrategy(getContext(), this.authority, this.mResourceId);
        }
        // ...
    }
    
    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        if (this.mStrategy == null) {
            this.mStrategy = getPathStrategy(getContext(), this.authority, this.mResourceId);
        }
        // ...
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        if (this.mStrategy == null) {
            this.mStrategy = getPathStrategy(getContext(), this.authority, this.mResourceId);
        }
        // ...
    }
```
