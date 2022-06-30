package com.shf.spring.circulardepend.setinjection;

/**
 * 如果您主要使用构造函数注入，则可能会创建无法解决的循环依赖场景。
 * 避免构造函数注入并仅使用 setter 注入。也就是说，虽然不推荐，但是可以通过setter注入来配置循环依赖。
 */
public class ClientSet {
    public static void main(String[] args) {
        ServiceAA aa = new ServiceAA();
        ServiceBB bb = new ServiceBB();

        aa.setServiceBB(bb);
        bb.setServiceAA(aa);
    }
}
