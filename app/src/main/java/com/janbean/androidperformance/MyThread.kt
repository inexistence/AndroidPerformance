package com.janbean.androidperformance

//import com.janbean.optimize.firebase.FirebaseExecutors
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

class MyThread {
    private val zza: ExecutorService? = null

    protected fun getBroadcastExecutor(): Executor? {
//        return FirebaseExecutors.getExecutorForCloudMessagingReceiver(this.zza)
        return this.zza
    }
}