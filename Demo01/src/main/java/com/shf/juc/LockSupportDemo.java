package com.shf.juc;

import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 3种让线程等待和唤醒的方法
 * 方式1:  使用Object中的wait()方法让线程等待， 使用Object中的notify()方法唤醒线程
 * 方式2:  使用JUC包中Condition的await()方法让线程等待，使用signal()方法唤醒线程
 * 方式3:  LockSupport类可以阻塞当前线程以及唤醒指定被阻塞的线程
 *
 *传统的synchronized和Lock实现等待唤醒通知的约束
 * 线程先要获得并持有锁，必须在锁块（synchronized或lock）中
 * 必须要先等待后唤醒，线程才能够被唤醒
 *
 * LockSupport类中的park等待和unpark唤醒
 * 通过park()和unpark(thread)方法来实现阻塞和唤醒线程的操作
 *
 * 为什么可以先唤醒线程后阻塞线程？
 * 因为unpark获得了一个凭证，之后在调用park方法，就可以名正言顺的凭证消费
 *
 * 为什么唤醒两次后阻塞两次，但最终结果还是会阻塞线程？
 * 因为凭证数量最多为1，连续调用两次unpark和调用一次unpark效果一样，
 * 只会增加一个凭证，而调用两次park却需要消费两个凭证，证不够，不能放行
 */
public class LockSupportDemo {
    static Object objectLock = new Object();
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) {
//        Object类中的wait和notify方法实现线程等待和唤醒
//        synchronizedWaitNotify();

//        Condition接口中的await后signal方法实现线程的等待和唤醒
//        conditionAwaitSignal();

        lockSupport();
    }

    /**
     * Object类中的wait和notify方法实现线程等待和唤醒
     * wait和notify方法必须要在同步块或者方法里面且成对出现使用
     * Object类中的wait、notify、notifyALlL用于线程等待和唤醒的方法，都必须在synchronized内部执行（必须用到关键字synchronized)
     * 先wait后notify才OK,否则无法唤醒
     *
     * Exception in thread "B" java.lang.IllegalMonitorStateException
     */
    private static void synchronizedWaitNotify() {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            synchronized (objectLock){
                System.out.println(Thread.currentThread().getName()+"\t ----- come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName()+"\t---- 被唤醒");
            }
        },"A").start();


        new Thread(()->{
            synchronized (objectLock){
                objectLock.notify();
                System.out.println(Thread.currentThread().getName()+"\t -- 通知");
            }
        },"B").start();
    }

    /**
     * Condition接口中的await后signal方法实现线程的等待和唤醒
     */
    private static void conditionAwaitSignal(){
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"\t ---- come in");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName()+"\t ----- 被唤醒");
            } finally {
                lock.unlock();
            }
        },"A").start();


        new Thread(()->{
            lock.lock();
            try {
               condition.signal();
                System.out.println(Thread.currentThread().getName()+"\t ----通知");
            } finally {
                lock.unlock();
            }
        },"B").start();
    }


    private static void lockSupport(){
        Thread a = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getName() + "\t ---- come in"+System.currentTimeMillis());
            LockSupport.park(); // 被阻塞...等待通知等待放行，它要通过需要许可证
            LockSupport.park(); // 被阻塞...等待通知等待放行，它要通过需要许可证
            System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒"+System.currentTimeMillis());
        }, "A");
        a.start();

        Thread b = new Thread(() -> {
            LockSupport.unpark(a); // 增加一个凭证，但凭证最多只能有一个，累加无效
            LockSupport.unpark(a);
            System.out.println(Thread.currentThread().getName() + "\t ---- 通知");
        }, "B");
        b.start();
    }
}
