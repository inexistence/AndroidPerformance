package com.janbean.androidperformance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Test2 {
        private static ExecutorService service = Executors.newFixedThreadPool(5);
    private static ExecutorService service5 = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setName("Hello");
        return t;
    });

//    private static ExecutorService service2 = Executors.newSingleThreadExecutor();

    public static void test() {
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
    }
}
