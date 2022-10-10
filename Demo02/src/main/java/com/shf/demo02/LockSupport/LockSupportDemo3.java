package com.shf.demo02.LockSupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。
 * LockSupport是一个线程阻塞工具类，所有的方法都是静态方法，可以让线程在任意位置阻塞，阻寒之后也有对应的唤醒方法。归根
 * 结底，LockSupport调用 的Unsafe中的native代码。
 * LockSupport提供park()和unpark()方法实现阻塞线程和解除线程阻塞的过程
 * LockSupport和每个使用它的线程都有一个许可(permit)关联。permit相当于1， 0的开关，默认是0,
 * 调用一次unpark就加1变成1,
 * 调用一次park会消费permit,也就是将1变成0，同时park立即返回。
 * 如再次调用park会变成阻塞(因为permit为零了会阻塞在这里，一直 到permit变为1)，这时调用unpark会把permit置为1。
 * 每个线程都有一个相关的permit, permit最多只有一个，重复调用unpark也不会积累凭证。
 *
 * 形象的理解
 * 线程阻塞需要消耗凭证(permit)，这个凭证最多只有1个。
 * 当调用park方法时
 * *    如果有凭证，则会直接消耗掉这个凭证然后正常退出;
 * *    如果无凭证，就必须阻塞等待凭证可用;
 * 而unpark则相反，它会增加一个凭证，但凭证最多只能有1个，累加无效。
 *
 *
 * 为什么可以先唤醒线程后阻塞线程?
 * 因为unpark获得了一个凭证，之后再调用park方法，就可以名正言顺的凭证消费，故不会阻塞。
 *
 * 为什么唤醒两次后阻塞两次，但最终结果还会阻塞线程?
 * 因为凭证的数量最多为1，连续调用两次unpark和调用一次unpark效果--样，只会增加一个凭证;
 * 而调用两次park却需要消费两个凭证，证不够，不能放行。
 */
public class LockSupportDemo3 {

    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getName() + "\t ---- come in");
            LockSupport.park();
//            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t ----- 被唤醒");
        },"a");
        a.start();

        Thread b = new Thread(() -> {
            LockSupport.unpark(a);
//            LockSupport.unpark(a); //permit最多只有一个，重复调用unpark也不会积累凭证。
            System.out.println(Thread.currentThread().getName() + "\t" + "---- 通知了");
        }, "b");
        b.start();
    }
}
