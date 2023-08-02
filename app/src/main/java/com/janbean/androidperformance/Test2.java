package com.janbean.androidperformance;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.janbean.androidperformance.test.ExecutorScheduler;
import com.janbean.androidperformance.test.NamedThreadFactory;
import com.janbean.androidperformance.test.UnIdleHandlerThread;

public class Test2 {
    private static ExecutorService service = Executors.newFixedThreadPool(5);
    private static ExecutorService service5 = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setName("Hello");
        return t;
    });

    public static void config(MyThread thread) {

    }

    private static ExecutorService executorService = new ThreadPoolExecutor(1, 1, 1,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory("", 1));

    private static Thread thread = new MyThread(new Runnable() {

        @Override
        public void run() {

        }
    });

    private static Handler handler;

    private static Handler getHandler() {
        if (handler == null) {
            HandlerThread handlerThread = new UnIdleHandlerThread("imo_camera_1");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }
        return handler;
    }

//    private static ExecutorService service2 = Executors.newSingleThreadExecutor();

    public static void test() {
        try {
            thread.start();
        } catch (Exception e) {

        }

        new ExecutorScheduler().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Log.d("h", "bbb");
                } catch (InterruptedException e) {

                }
            }
        });
        service5.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
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

        getHandler().post(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            System.out.println("test3");
        });
    }
}
