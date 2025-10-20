package com.devmaster.Day01Test.lambda_expression;

public class LambdaExpression2 {
    public static void main(String[] args) {
        // Dạng đầy đủ
        SayHello2 say1 = (name) -> {
            System.out.println("Hello " + name);
        };
        say1.sayHello("Devmaster");

        // Ngắn gọn
        SayHello2 say2 = name -> {
            System.out.println("Hello " + name);
        };
        say2.sayHello("Devmaster");

        // Ngắn gọn hơn (1 câu lệnh)
        SayHello2 say3 = name -> System.out.println("Hello " + name);
        say3.sayHello("Devmaster");
    }
}