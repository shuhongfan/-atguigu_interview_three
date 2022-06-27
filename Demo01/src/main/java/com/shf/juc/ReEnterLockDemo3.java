package com.shf.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReEnterLockDemo3 {
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(()->{
            lock.lock();
            try {
                System.out.println("======== 外层");
                lock.lock();
                try {
                    System.out.println("====== 内层");
                } finally {
//                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        },"t1").start();

        new Thread(()->{
            lock.lock();
            try {
                System.out.printf(Thread.currentThread().getName()+"\t-----调用开始");
            } finally {
                lock.unlock();
            }
        },"t2").start();
    }
}
