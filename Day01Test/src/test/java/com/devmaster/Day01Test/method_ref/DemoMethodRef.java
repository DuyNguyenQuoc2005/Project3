package com.devmaster.Day01Test.method_ref;

import java.util.Arrays;

public class DemoMethodRef {
    public static void main(String[] args) {
        int a = 10, b = 20;

        // 1) Tham chiếu static method
        int sum   = doAction(a, b, MathUtils::sum);
        System.out.println(a + " + " + b + " = " + sum);

        int minus = doAction(a, b, MathUtils::minus);
        System.out.println(a + " - " + b + " = " + minus);

        // 2) Tham chiếu instance method của một đối tượng cụ thể
        MathUtils mathUtils = new MathUtils();
        int mul = doAction(a, b, mathUtils::multiply);
        System.out.println(a + " * " + b + " = " + mul);

        // 3) Tham chiếu instance method của một kiểu bất kỳ (kiểu String)
        String[] arr = {"Java", "C++", "PHP", "C#", "Javascript"};
        Arrays.sort(arr, String::compareToIgnoreCase); // thay cho (s1, s2) -> s1.compareToIgnoreCase(s2)
        for (String s : arr) System.out.println(s);
    }

    public static int doAction(int a, int b, ExecuteFunction func) {
        return func.execute(a, b);
    }
}