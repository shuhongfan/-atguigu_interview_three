package com.shf.spring.circulardepend.setinjection;

public class ServiceBB {
    private ServiceAA serviceAA;

    public void setServiceAA(ServiceAA serviceAA) {
        this.serviceAA = serviceAA;
        System.out.println("B 里面设置了 A");
    }
}
