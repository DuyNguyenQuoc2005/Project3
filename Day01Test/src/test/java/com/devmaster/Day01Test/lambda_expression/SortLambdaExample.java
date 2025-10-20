package com.devmaster.Day01Test.lambda_expression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortLambdaExample {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java SpringBoot", "C# NetCore", "PHP", "JavaScript");

        // Sắp xếp tăng dần dùng lambda (so sánh 2 chuỗi)
        Collections.sort(list, (String str1, String str2) -> str1.compareTo(str2));

        for (String str : list) {
            System.out.println(str);
        }
    }
}