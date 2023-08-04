### okhttp全局默认配置

#### 修改点 

通过asm修改字节码，在 `OkHttpClient.Builder` 构造函数的最后，调用 `GlobalOkHttpBuilder.configBuilder`，使得项目所有 `OKHttpClient` 都会配置自定义的参数。

#### 使用
1. 设置 plugins
```
plugins {
    id("OptimizeOKHttpClientPlugin")
}
```

2. 找个时机自定义配置，需要在首次使用okhttp3之前。 参考： `Application.onCreate` 时，控制所有 `OkHttpClient` 统一默认线程池
```
class MyApplication: Application() {
    companion object {
        private val okHttpDispatcher by lazy { Dispatcher() }
        private val okHttpConnectionPool by lazy { ConnectionPool() }
    }

    override fun onCreate() {
        super.onCreate()
    
        GlobalOkHttpBuilder.setDefaultBuilderConfigure(object : BuilderConfigure {
            override fun configDefaultBuilder(builder: OkHttpClient.Builder) {
                builder.dispatcher(okHttpDispatcher)
                builder.connectionPool(okHttpConnectionPool)
            }
        })
    }
}
```