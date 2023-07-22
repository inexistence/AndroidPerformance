## FileProvider修改代码参考

```
    @Override // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo info) {
        if (info.grantUriPermissions) {
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
        throw new SecurityException("Provider must grant uri permissions");
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
