package com.shf.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AQSDemo {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

//        3个线程模拟3个来银行网点，受理窗口办理业务员的顾客
        new Thread(()->{
            lock.lock();
            try {
                System.out.println("------------ A thread come in");

                TimeUnit.MINUTES.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        },"A").start();

//        第二个顾客，第二个线程，由于受理业务的窗口只能有一个，此时B只能等待
        new Thread(()->{
            lock.lock();
            try {
                System.out.println("------------ B thread come in");
            } finally {
                lock.unlock();
            }
        },"B").start();

        new Thread(()->{
            lock.lock();
            try {
                System.out.println("------------ C thread come in");
            } finally {
                lock.unlock();
            }
        },"C").start();
    }
}
