package com.devmaster.Day01Test.lambda_expression;

public class LambdaExpression1 {
    public static void main(String[] args) {
        // lambda không tham số
        SayHello1 sayHello = () -> {
            System.out.println("Hello World");
        };
        sayHello.sayHello();
    }
}