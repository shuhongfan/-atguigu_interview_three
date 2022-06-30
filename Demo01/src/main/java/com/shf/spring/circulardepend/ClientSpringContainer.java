package com.shf.spring.circulardepend;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Caused by: org.springframework.beans.factory.BeanCurrentlyInCreationException:
 * Error creating bean with name 'a': Requested bean is currently in creation
 */
public class ClientSpringContainer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        A a = context.getBean("a", A.class);
        B b = context.getBean("B", B.class);
    }
}
