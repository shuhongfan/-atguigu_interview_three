package com.shf.redis02.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GoodController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Redisson redisson;

    @Value("${server.port}")
    private String serverPort;

    public static final String REDIS_LOCK = "shuhongfan";

    @GetMapping("/buy_goods")
    public String buy_Goods() {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

        RLock redissonLock = redisson.getLock(REDIS_LOCK);
        redissonLock.lock();

        try {
            String result = redisTemplate.opsForValue().get("goods:001");
            int goodsNumber = result == null ? 0 : Integer.parseInt(result);

            if (goodsNumber > 0) {
                int realNumber = goodsNumber - 1;
                redisTemplate.opsForValue().set("goods:001", String.valueOf(realNumber));
                System.out.println("成功买到商品，库存还剩下： " + realNumber + " 件\t服务提供端口：" + serverPort);
                return "成功买到商品，库存还剩下： " + realNumber + " 件\t服务提供端口：" + serverPort;
            } else {
                System.out.println("商品已经售完，活动结束，欢迎下次光临，\t 服务提供端口：" + serverPort);
                return "商品已经售完，活动结束，欢迎下次光临，\t 服务提供端口：" + serverPort;
            }
        } finally {
            // 还在持有锁的状态，并且是当前线程持有的锁再解锁
            if (redissonLock.isLocked() && redissonLock.isHeldByCurrentThread()) {
                redissonLock.unlock();
            }
        }
    }
}
