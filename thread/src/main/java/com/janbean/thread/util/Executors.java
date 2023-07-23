package com.janbean.thread.util;

import android.util.Log;

import com.janbean.thread.KeepThread;
import com.janbean.thread.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

@KeepThread
public class Executors {
    public static ExecutorService newSingleThreadExecutor(String name) {
        NamedThreadFactory factory = new NamedThreadFactory(name);
        factory.type = "SingleThread";
        // TODO 自己实现，好计算任务耗时
        return java.util.concurrent.Executors.newSingleThreadExecutor(factory);
    }
}
