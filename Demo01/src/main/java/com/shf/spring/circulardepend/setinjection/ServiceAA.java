package com.shf.spring.circulardepend.setinjection;

public class ServiceAA {
    private ServiceBB serviceBB;

    public void setServiceBB(ServiceBB serviceBB) {
        this.serviceBB = serviceBB;
        System.out.println("A 里面设置了 B");
    }
}
