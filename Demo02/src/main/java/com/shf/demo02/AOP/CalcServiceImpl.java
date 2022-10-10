package com.shf.demo02.AOP;

public class CalcServiceImpl implements CalcService{
    @Override
    public int div(int x, int y) {
        int result = x / y;
        System.out.println("=============> CalcServiceImpl被调用了，我们的计算结果：" + result);
        return result;
    }
}
