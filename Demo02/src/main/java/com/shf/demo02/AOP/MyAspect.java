package com.shf.demo02.AOP;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Before("execution(com.shf.demo02.AOP.CalcServiceImpl.*(..))")
    public void beforeNotify() {
        System.out.println("****** @Before我是前置通知MyAspect");
    }

    @After("execution(com.shf.demo02.AOP.CalcServiceImpl.*(..))")
    public void afterNotify() {
        System.out.println("********* @After 我是后置通知");
    }

    @AfterReturning("execution(com.shf.demo02.AOP.CalcServiceImpl.*(..))")
    public void afterReturningNotify() {
        System.out.println("********* @AfterReturning 我是返回后通知");
    }

    @AfterThrowing("execution(com.shf.demo02.AOP.CalcServiceImpl.*(..))")
    public void afterReturningThrowing() {
        System.out.println("********* @AfterReturning 我是异常通知");
    }

    @SneakyThrows
    @Around("execution(com.shf.demo02.AOP.CalcServiceImpl.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        Object retValue = null;
        System.out.println("我是环绕通知之前AAA");
        retValue = proceedingJoinPoint.proceed();
        System.out.println("我是环绕通知之后BBB");
        return retValue;
    }

}
