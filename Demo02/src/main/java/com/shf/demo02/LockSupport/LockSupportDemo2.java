package com.shf.demo02.LockSupport;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。
 * LockSupport类使用了-●种 名为Permit (许可)的概念来做到阻塞和唤醒线程的功能，每个线程都有一- 个许可(permit),
 * permit只有两个值1和零，默认是零。.
 * 可以把许可看成是一一种(0,1)信 号量(Semaphore)，但与Semaphore不同的是，许可的累加上限是1。
 *
 *
 */
public class LockSupportDemo2 {
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        new Thread(()->{
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"\t"+"--come in");
                condition.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }).start();



    }
}
