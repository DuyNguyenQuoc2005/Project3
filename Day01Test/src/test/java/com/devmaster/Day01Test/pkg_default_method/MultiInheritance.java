package com.devmaster.Day01Test.pkg_default_method;

public class MultiInheritance implements Interface1, Interface2 {

    // BẮT BUỘC override để giải quyết xung đột method1()
    @Override
    public void method1() {
        // Chọn rõ ràng default method của Interface1 (hoặc Interface2 tuỳ bạn)
        Interface1.super.method1();
        // Nếu muốn chọn Interface2 thì thay bằng: Interface2.super.method1();
        // Hoặc tự in ra logic riêng của lớp này.
    }
}