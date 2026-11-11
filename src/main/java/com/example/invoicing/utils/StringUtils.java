package com.example.invoicing.utils;

public class StringUtils {
    public static String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text.toLowerCase().trim();
    }
}
