package com.devmaster.Day01Test.lambda_expression;

public class LambdaExpression3 {
    public static void main(String[] args) {
        Calculator1 calc1 = (int a, int b) -> (a + b);
        System.out.println(calc1.add(11, 12));

        Calculator1 calc2 = (a, b) -> (a + b);
        System.out.println(calc2.add(21, 22));

        Calculator2 calc4 = (a, b) -> {
            int sum = a + b;
            System.out.println(a + " + " + b + " = " + sum);
        };
        calc4.add(41, 42);
    }
}