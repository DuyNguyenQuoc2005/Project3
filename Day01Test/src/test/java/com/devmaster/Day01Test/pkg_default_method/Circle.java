package com.devmaster.Day01Test.pkg_default_method;


public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("Vẽ hình tròn");
    }
    // có thể giữ nguyên setColor() mặc định; hoặc override nếu muốn
}