package com.shf.demo02.ReEnterLock;

/**
 * 隐式锁( 即synchronized关键字使用的锁)默认是可重入锁
 */
public class ReEnterLockDemo {
    static Object objectLockA = new Object();

    public static void m1() {
        new Thread(()->{
            synchronized (objectLockA) {
                System.out.println(Thread.currentThread().getName()+"\t 外层调用");
                synchronized (objectLockA) {
                    System.out.println(Thread.currentThread().getName()+"\t 中层调用");
                    synchronized (objectLockA) {
                        System.out.println(Thread.currentThread().getName()+"\t 内层调用");
                    }
                }
            }
        }).start();
    }


    public static void main(String[] args) {
        m1();
    }
}
