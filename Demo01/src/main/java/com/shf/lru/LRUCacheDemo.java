package com.shf.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheDemo<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    public LRUCacheDemo(int capacity) {
        /**
         * initialCapacity - 初始容量
         * loadFactor – 负载因子
         * accessOrder – 排序模式 - 访问顺序为true ，插入顺序为false
         */
        super(capacity, 0.75F, false);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return super.size() > capacity;
    }

    public static void main(String[] args) {
        LRUCacheDemo<Integer, Object> lruCacheDemo = new LRUCacheDemo(3);
        lruCacheDemo.put(1, "a");
        lruCacheDemo.put(2, "b");
        lruCacheDemo.put(3, "c");

        System.out.println(lruCacheDemo.keySet());

        lruCacheDemo.put(4, "d");
        System.out.println(lruCacheDemo.keySet());

        lruCacheDemo.put(3, "c");
        System.out.println(lruCacheDemo.keySet());
        lruCacheDemo.put(3, "c");
        System.out.println(lruCacheDemo.keySet());
        lruCacheDemo.put(3, "c");
        System.out.println(lruCacheDemo.keySet());

        lruCacheDemo.put(5, "x");
        System.out.println(lruCacheDemo.keySet());
    }
}
