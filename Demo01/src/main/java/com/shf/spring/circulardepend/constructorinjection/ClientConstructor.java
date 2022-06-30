package com.shf.spring.circulardepend.constructorinjection;

/**
 * 如果您主要使用构造函数注入，则可能会创建无法解决的循环依赖场景。
 * <p>
 * 例如：A类通过构造函数注入需要B类的实例，B类通过构造函数注入需要A类的实例。
 * 如果你为类 A 和 B 配置 bean 以相互注入，Spring IoC 容器会在运行时检测到这个循环引用，
 * 并抛出一个 BeanCurrentlyInCreationException.
 * <p>
 * 一种可能的解决方案是编辑某些类的源代码以由设置器而不是构造器配置。
 * 或者，避免构造函数注入并仅使用 setter 注入。
 * 也就是说，虽然不推荐，但是可以通过setter注入来配置循环依赖。
 * <p>
 * 与典型情况（没有循环依赖关系）不同，bean A 和 bean B 之间的循环依赖关系强制其中一个 bean 在完全初始化之前注入另一个 bean（典型的先有鸡还是先有蛋的场景）。
 */
public class ClientConstructor {
    public static void main(String[] args) {
//        new ServiceA(new ServiceB(new ServiceA()))
    }
}
