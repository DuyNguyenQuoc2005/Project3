package com.devmaster.Day01Test.pkg_default_method;

public interface Interface2 {
    default void method1() { // trùng chữ ký với Interface1
        System.out.println("Interface2.method1()");
    }
    default void method2() {
        System.out.println("Interface2.method2()");
    }
}