package com.shf.spring.aop;

import org.springframework.stereotype.Service;

@Service
public class CalServiceImpl implements CalcService {
    @Override
    public int div(int x, int y) {
        int result = x / y;
        System.out.println("============> CalServiceImpl 被调用，我们的计算结果：" + result);
        return result;
    }
}
