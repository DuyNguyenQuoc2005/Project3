package com.devmaster.Day01Test.pkg_collection_api_enhencements;

import java.util.Arrays;
import java.util.List;

public class ForEachListExample {
    public static void main(String[] args) {
        List<String> languages = Arrays.asList("Java Spring", "C#", "NetCore API", "PHP Laravel", "Javascript");

        System.out.println("Sử dụng biểu thức Lambda:");
        languages.forEach(lang -> System.out.println(lang));

        System.out.println("Sử dụng method reference:");
        languages.forEach(System.out::println);
    }
}