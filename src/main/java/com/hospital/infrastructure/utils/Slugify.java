package com.hospital.infrastructure.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;


public class Slugify {
    private static String removeVietnameseAccents(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
    public static String toSlug(String input) {
        return removeVietnameseAccents(input)
                .toLowerCase()
                .replaceAll("đ", "d")
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }
}
