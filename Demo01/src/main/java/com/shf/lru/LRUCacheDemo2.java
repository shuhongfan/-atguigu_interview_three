package com.shf.lru;

import java.util.HashMap;
import java.util.Map;

public class LRUCacheDemo2 {
//    map负责查找，构建一个虚拟的双向链表，它里面安装的就是一个Node接口，作为数据载体

    //    1. 构造一个Node节点，作为数据载体
    class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        public Node() {
            this.prev = this.next = null;
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.prev = this.next = null;
        }
    }

    //    2.构造一个虚拟双向链表，里面安放的就是我们的Node
    class DoubleLinkedList<K, V> {
        Node<K, V> head;
        Node<K, V> tail;

        //        2.1 构造方法
        public DoubleLinkedList() {
            head = new Node<>();
            tail = new Node<>();
            head.next = tail;
            tail.prev = head;
        }

        //        2.2 添加到头
        public void addHead(Node<K, V> node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        //        2.3 删除节点
        public void removeNode(Node<K, V> node) {
            node.next.prev = node.prev;  // 绕过节点
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;
        }

        //        2.4 获得最后一个节点
        public Node getLast() {
            return tail.prev;
        }

    }

    private int cacheSize;
    Map<Integer, Node<Integer, Integer>> map;
    DoubleLinkedList<Integer, Integer> doubleLinkedList;

    public LRUCacheDemo2(int cacheSize) {
        this.cacheSize = cacheSize;
        map = new HashMap<>();
        doubleLinkedList = new DoubleLinkedList<>();
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        Node<Integer, Integer> node = map.get(key);
        doubleLinkedList.removeNode(node);  // 先删除
        doubleLinkedList.addHead(node); // 再添加
        return node.value;
    }

    //    saveOrUpdate method
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node<Integer, Integer> node = map.get(key);
            node.value = value;
            map.put(key, node);

            doubleLinkedList.removeNode(node);
            doubleLinkedList.addHead(node);
        } else {
            if (map.size() == cacheSize) {  // 坑位满了
                Node<Integer, Integer> lastNode = doubleLinkedList.getLast();
                map.remove(lastNode.key);
                doubleLinkedList.removeNode(lastNode);
            }
//            新增
            Node<Integer, Integer> newNode = new Node<>(key, value);
            map.put(key, newNode);
            doubleLinkedList.addHead(newNode);
        }
    }

    public static void main(String[] args) {
        LRUCacheDemo2 lruCacheDemo = new LRUCacheDemo2(3);
        lruCacheDemo.put(1, 1);
        lruCacheDemo.put(2, 2);
        lruCacheDemo.put(3, 3);

        System.out.println(lruCacheDemo.map.keySet());

        lruCacheDemo.put(4, 4);
        System.out.println(lruCacheDemo.map.keySet());

        lruCacheDemo.put(3, 3);
        System.out.println(lruCacheDemo.map.keySet());
        lruCacheDemo.put(3, 3);
        System.out.println(lruCacheDemo.map.keySet());
        lruCacheDemo.put(3, 3);
        System.out.println(lruCacheDemo.map.keySet());

        lruCacheDemo.put(5, 5);
        System.out.println(lruCacheDemo.map.keySet());
    }
}
