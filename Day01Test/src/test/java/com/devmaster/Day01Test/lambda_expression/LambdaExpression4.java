package com.devmaster.Day01Test.lambda_expression;

import java.util.Arrays;
import java.util.List;

public class LambdaExpression4 {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java SpringBoot", "C# NetCore", "PHP", "JavaScript");

        // Dùng lambda với forEach
        list.forEach(item -> System.out.println(item));

        System.out.println("==================");

        // Method reference (ngắn hơn)
        list.forEach(System.out::println);
    }
}