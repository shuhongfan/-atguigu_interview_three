package com.shf.demo02.ReEnterLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个线程可以多次获取同一把锁
 */
public class ReEnterLockDemo3 {
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(()->{

            try {
                System.out.println("外层");
                lock.lock();
                try {
                    System.out.println("内层");
                    lock.lock();
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(()->{
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"调用开始");
            } finally {
                lock.unlock();
            }
        }).start();
    }
}
