package com.shf.demo02.spring;

import org.springframework.stereotype.Component;

@Component
public class ServiceA {
    private ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
