package com.devmaster.Day01Test.method_ref;

public class MathUtils {
    public MathUtils() {}
    public MathUtils(String str) {
        System.out.println("MathUtils: " + str);
    }

    public static int sum(int a, int b)    { return a + b; }
    public static int minus(int a, int b)  { return a - b; }
    public int multiply(int a, int b)      { return a * b; }
}