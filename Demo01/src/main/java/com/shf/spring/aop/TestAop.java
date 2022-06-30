package com.shf.spring.aop;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;

@SpringBootTest
public class TestAop {
    @Autowired
    private CalcService calcService;

    @Test
    public void testAop() {
        System.out.println("spring版本:" + SpringVersion.getVersion() + "\t springboot版本：" + SpringBootVersion.getVersion());

        calcService.div(10, 2);
    }
}
