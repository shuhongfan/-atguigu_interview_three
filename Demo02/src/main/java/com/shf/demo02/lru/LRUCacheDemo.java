package com.shf.demo02.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheDemo<K,V> extends LinkedHashMap<K,V> {
    private int capacity;

    public LRUCacheDemo(int capacity) {
        super(capacity,0.75F,false);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return super.size() > capacity;
    }

    public static void main(String[] args) {
        LRUCacheDemo<Integer, Object> lruCacheDemo = new LRUCacheDemo<>(3);

        lruCacheDemo.put(1, "A");
        lruCacheDemo.put(2, "B");
        lruCacheDemo.put(3, "C");

        System.out.println(lruCacheDemo.keySet());

        lruCacheDemo.put(4, "D");
        System.out.println(lruCacheDemo.keySet());

        lruCacheDemo.put(3, "C");
        System.out.println(lruCacheDemo.keySet());

        lruCacheDemo.put(3, "C");
        System.out.println(lruCacheDemo.keySet());

        lruCacheDemo.put(3, "C");
        System.out.println(lruCacheDemo.keySet());

        lruCacheDemo.put(3, "C");
        System.out.println(lruCacheDemo.keySet());
    }
}
