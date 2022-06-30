package com.shf.redis01.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 1.synchronized      单机版oK，上分布式
 * 2.nginx分布式微服务 单机锁不行
 * 3.取消单机锁         上redis分布式锁setnx
 * 4.只加了锁，没有释放锁，  出异常的话，可能无法释放锁， 必须要在代码层面finally释放锁
 * 5.宕机了，部署了微服务代码层面根本没有走到finally这块，没办法保证解锁，这个key没有被删除，需要有lockKey的过期时间设定
 * 6.为redis的分布式锁key，增加过期时间。此外，还必须要setnx+过期时间必须同一行的原子性操作
 * 7.必须规定只能自己删除自己的锁，你不能把别人的锁删除了,防止张冠李戴，1删2,2删3
 * 8.lua或者事务
 * 9.redis集群环境下，我们自己写的也不OK，直接上RedLock之Redisson落地实现
 */
@RestController
public class GoodController {
    public static final String REDIS_LOCK = "shuhongfan";
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private Redisson redisson;

    @GetMapping("/buy_goods")
    public String buy_Goods() throws Exception {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        RLock redissonLock = redisson.getLock(REDIS_LOCK);
        redissonLock.lock();
        try {
//       3.0 分布式部署后，单机锁还是出现超卖现象，需要分布式锁  加锁  setIfAbsent() 就是如果不存在就新建
//            Boolean flag = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value); // setNX

//       5.0 部署了微服务jar包的机器挂了，代码层面根本没有走到finally这块， 没办法保证解锁，这个key没有被删除，需要加入一个过期时间限定key
//            redisTemplate.expire(REDIS_LOCK, 10L, TimeUnit.SECONDS);

//           6.0 设置key+过期时间分开了，必须要合并成一行具备原子性
            Boolean flag = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value, 10L, TimeUnit.SECONDS);

            if (!flag) {
                return "抢锁失败";
            }
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
        } finally {  // 4.0 出异常的话，可能无法释放锁， 必须要在代码层面finally释放锁
            //  7.0 张冠李戴，删除了别人的锁       解锁
//            if (redisTemplate.opsForValue().get(REDIS_LOCK).equalsIgnoreCase(value)) { // 只能自己删除自己的，不许动别人的
//                redisTemplate.delete(REDIS_LOCK);
//            }

//           8.0 finally块的判断+del删除操作不是原子性的,通过redis自身的事务
//            while (true) {
//                redisTemplate.watch(REDIS_LOCK);
//                if (redisTemplate.opsForValue().get(REDIS_LOCK).equalsIgnoreCase(value)) {
//                    redisTemplate.setEnableTransactionSupport(true);
//                    redisTemplate.multi();
//                    redisTemplate.delete(REDIS_LOCK);
//                    List<Object> list = redisTemplate.exec();
//                    if (list == null) {
//                        continue;
//                    }
//                }
//                redisTemplate.unwatch();
//                break;
//            }

//            9.0  用Lua脚本  Redis可以通过eval命令保证代码执行的原子性
//            Jedis jedis = RedisUtils.getJedis();
//            String script = "if redis.call('get', KEYS[1]) == ARGV[1]"+"then "
//                    +"return redis.call('del', KEYS[1])"+"else "+ "  return 0 " + "end";
//            try{
//                Object result = jedis.eval(script, Collections.singletonList(REDIS_LOCK), Collections.singletonList(value));
//                if ("1".equals(result.toString())){
//                    System.out.println("------del REDIS_LOCK_KEY success");
//                }else {
//                    System.out.println("------del REDIS_LOCK_KEY error");
//                }
//            }finally {
//                if (null != jedis){
//                    jedis.close();
//                }
//            }

//            10.0 上Redisson
//            redis异步复制造成的锁丢失， 比如:主节点没来的及把刚刚set进来这条数据给从节点，就挂了。此时如果集群模式下，就得上Redisson来解决
            // 判断还在持有锁的状态，并且是当前线程持有的锁再解锁
            if (redissonLock.isLocked() && redissonLock.isHeldByCurrentThread()) {
                redissonLock.unlock();
            }
        }
    }
}
