package com.devmaster.Day01Test.pkg_collection_api_enhencements;

import java.util.HashMap;
import java.util.Map;

public class ForEachMapExample {
    public static void main(String[] args) {
        Map<Integer, String> hmap = new HashMap<>();
        hmap.put(1, "Java Spring");
        hmap.put(2, "Javascript");
        hmap.put(3, "PHP Laravel");
        hmap.put(4, "C# NetCore");

        // Duyệt key/value bằng forEach của Map
        hmap.forEach((key, value) -> System.out.println(key + " - " + value));
    }
}