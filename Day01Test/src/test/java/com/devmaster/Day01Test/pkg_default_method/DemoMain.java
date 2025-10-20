package com.devmaster.Day01Test.pkg_default_method;

public class DemoMain {
    public static void main(String[] args) {
        // Test phần Shape
        Shape s1 = new Circle();
        s1.draw();           // "Vẽ hình tròn"
        s1.setColor("đỏ");   // dùng default method trong Shape

        // Test phần đa kế thừa default method
        MultiInheritance mi = new MultiInheritance();
        mi.method1();  // sẽ in theo lựa chọn trong override (Interface1.super.method1())
        mi.method2();  // từ Interface2 (không xung đột, dùng trực tiếp)
    }
}