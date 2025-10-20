package com.devmaster.Day01Test.lambda_expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LambdaExpression5 {
    public static void main(String[] args) {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "Lập trình Java", 9.95f));
        books.add(new Book(2, "Java SpringBoot", 19.95f));
        books.add(new Book(3, "PHP Laravel", 12.95f));
        books.add(new Book(4, "NetCore API", 29.95f));
        books.add(new Book(5, "Javascript", 19.95f));

        // Lọc sách có giá > 15
        Stream<Book> filter = books.stream().filter(b -> b.price > 15);

        // In kết quả
        filter.forEach(System.out::println);
    }
}