package com.shf.demo02.LockSupport;

import java.util.concurrent.TimeUnit;

/**
 * synchronized  wait  notify
 * lock  await single
 *
 * LockSupport
 */
public class LockSupportDemo {
    static Object objectLock = new Object();

    public static void main(String[] args) {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName()+"\t come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName()+"\t 被唤醒");
            }
        },"A").start();

        new Thread(()->{
            synchronized (objectLock) {
                objectLock.notifyAll();
                System.out.println(Thread.currentThread().getName()+"\t通知");
            }
        },"B").start();
    }
}
