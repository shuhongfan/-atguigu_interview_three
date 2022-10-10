package com.shf.demo02.ReEnterLock;

/**
 * 每个锁对象拥有--个锁计数器和一个指向持有该锁的线程的指针。
 * 当执行monitorenter时，如果目标锁对象的计数器为零，那么说明它没有被其他线程所持有，Java虛拟机会将该锁对象的持有线程设
 * 置为当前线程，并且将其计数器加1。
 * 在目标锁对象的计数器不为零的情况下，如果锁对象的持有线程是当前线程，那么Java虚拟机可以将其计数器加1，否则需要等待
 * ，直至持有线程释放该锁。
 * 当执行monitorexit时，Java虚拟机则需将锁对象的计数器减1。计数器为零代表锁己被释放。
 */
public class ReEnterLockDemo2 {
    static Object objectLockA = new Object();

    public synchronized void m1() {
        System.out.println("外层");
        m2();
    }

    public synchronized void m2() {
        System.out.println("中层");
        m3();
    }

    public synchronized void m3() {
        System.out.println("内层");
    }

    public static void main(String[] args) {
        new ReEnterLockDemo2().m1();
    }

}
