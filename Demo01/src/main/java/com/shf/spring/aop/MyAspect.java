package com.shf.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Before("execution(public int com.shf.spring.aop.CalServiceImpl.*(..))")
    public void beforeNotify() {
        System.out.println("****** @Before我是前置通知MyAspect");
    }

    @After("execution(public int com.shf.spring.aop.CalServiceImpl..*(..))")
    public void afterNotify() {
        System.out.println("****** @After我是后置通知MyAspect");
    }

    @AfterReturning("execution(public int com.shf.spring.aop.CalServiceImpl.*(..))")
    public void afterReturningNotify() {
        System.out.println("****** @AfterReturning我是返回通知MyAspect");
    }

    @AfterThrowing("execution(public int com.shf.spring.aop.CalServiceImpl.*(..))")
    public void afterThrowingNotify() {
        System.out.println("****** @AfterThrowing我是异常通知MyAspect");
    }

    @Around("execution(public int com.shf.spring.aop.CalServiceImpl.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object retValue = null;
        System.out.println("我是环绕通知之前AAA");
        retValue = proceedingJoinPoint.proceed();
        System.out.println("我是环绕通知之后BBB");
        return retValue;
    }

}
