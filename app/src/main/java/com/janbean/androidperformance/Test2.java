package com.janbean.androidperformance;


import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test2 {
    private static ExecutorService service = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS, new SynchronousQueue());
    private static ExecutorService service2 = Executors.newSingleThreadExecutor();

    public static void test() {
        service2.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("hjianbin", "run singleThread "+Thread.currentThread().getName());
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {

                }
                System.out.println("test");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
                System.out.println("test2");
            }
        }, "test").start();

        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                System.out.println("test3");
            }
        });
    }
}
