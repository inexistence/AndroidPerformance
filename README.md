## Android性能优化

### 清单

**分析**
* - [ ] 线程使用追踪

**优化**
* - [x] [FileProvider优化](./buildSrc/README_FileProvider.md)
* - [ ] WorkManager优化
* - [ ] Firebase优化
* - [ ] 线程内存优化：减少线程初始化时申请的内存大小
* - [ ] 线程使用优化：
  * - [ ] 设置AsyncTask允许超时
  * - [ ] 专门开一个线程用来创建线程，因为创建线程也有开销
  * - [ ] 全局使用一个大线程池（可选）
* - [ ] SharedPreference卡顿优化

**其他了解点**
* Appsflyer
  * 性能较差，对启动耗时和卡顿有影响，经常在后台开线程做任务。
  * 如果官方为了查问题主动触发 remote debugger 数据上报，可能导致低端机 OOM/native 崩溃。
  * \>=6.6.0 <6.10.1 三星启动崩溃。
