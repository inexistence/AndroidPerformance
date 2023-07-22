## Android性能优化

### Plugin

#### FileProviderPlugin
```
plugins {
    id(libs.plugins.androidApplication.get().pluginId)
    id("FileProviderPlugin")
}
```
**启动耗时优化**

耗时点：
* FileProvider 初始化发生在 application.attachBaseContext 之后, application.onCreate 之前，其中 mStrategy 初始化较为耗时。

修改点：
* 通过asm修改字节码，在 FileProvider 初始化时设置 ProviderInfo.grantUriPermissions = false，使其不初始化 mStrategy。 同时修改为在使用 mStrategy 时判断是否为空，若为空再进行初始化。转移耗时到使用时。

[修改后代码参考](./buildSrc/FileProviderCodeSample.md)
