package com.shf.demo02.AQS;

import java.util.concurrent.locks.ReentrantLock;

/**
 * AQS是用来构建锁或者其它同步器组件的重量级基础框架及整个JUC体系的基石，通过内置的FIFO队列来完成资源获取线程的排队工作，并通过一每个int类型变量表示持有锁的状态
 *  state变量 + CLH变种的双端队列
 *
 *  Node = waitStatus + 前后指针的指向
 *
 * AQS acquire 主要有三条流程
 *  1. 调用 tryAcquire
 *  2. 调用 addWaiter
 *      双向链表中， 第一个节点为虚节点(也叫哨兵节点)， 其实并不存储任何信息，只是占位。真正的第一个有数据的节点，是从第二个节点开始的。
 *  3. 调用 acquireQueued
 */
public class AQSDemo {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();

        new Thread(()->{
            reentrantLock.lock();
            try {
                System.out.println("----------B thread come in");

            } finally {
                reentrantLock.unlock();
            }
        },"A").start();

    }
}
