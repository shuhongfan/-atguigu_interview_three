package com.shf.demo02.lru;

import java.util.HashMap;
import java.util.Map;

public class LRUCacheDemo2 {
    class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;
        public Node() {}

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.prev = this.next = null;
        }

    }

    //2.构建一个虚拟的双向链表，里面安放的就是我们的Node
    class DoubleLikedList<K, V> {
        Node<K, V> head;
        Node<K, V> tail;

        //2.1 构造方法
        public DoubleLikedList() {
            head = new Node<>();
            tail = new Node<>();
            head.next = tail;
            tail.prev = head;
        }

        //2.2 添加到头
        public void addHead(Node<K, V> node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        //2.3 删除节点
        public void removeNode(Node<K, V> node) {
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;
        }

        //2.4 获得最后一个节点
        public Node getLast() {
            return tail.prev;
        }
    }

    private int cacheSize;
    Map<Integer, Node<Integer, Integer>> map;
    DoubleLikedList<Integer, Integer> doubleLikedList;

    public LRUCacheDemo2(int cacheSize) {
        this.cacheSize = cacheSize;
        map = new HashMap<>();
        doubleLikedList = new DoubleLikedList<>();
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }

        Node<Integer, Integer> node = map.get(key);
        doubleLikedList.removeNode(node);
        doubleLikedList.addHead(node);

        return node.value;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node<Integer, Integer> node = map.get(key);
            node.value = value;
            map.put(key, node);

            doubleLikedList.removeNode(node);
            doubleLikedList.addHead(node);
        } else {
            if (map.size() == cacheSize) {
                Node<Integer,Integer> lastNode = doubleLikedList.getLast();
                map.remove(lastNode.key);
                doubleLikedList.removeNode(lastNode);
            }

            Node<Integer, Integer> newNode = new Node<Integer, Integer>(key, value);
            map.put(key, newNode);
            doubleLikedList.addHead(newNode);
        }
    }

}
