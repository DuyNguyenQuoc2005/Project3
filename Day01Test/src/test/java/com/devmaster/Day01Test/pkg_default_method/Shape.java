package com.devmaster.Day01Test.pkg_default_method;

public interface Shape {
    void draw(); // phương thức trừu tượng - lớp con bắt buộc viết

    default void setColor(String color) { // default method - KHÔNG bắt buộc override
        System.out.println("Vẽ hình với màu: " + color);
    }
}