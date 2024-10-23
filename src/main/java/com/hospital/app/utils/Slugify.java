package com.hospital.app.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;


public class Slugify {
    private static String removeVietnameseAccents(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
    public static String toSlug(String input) {
        String slug = removeVietnameseAccents(input).toLowerCase();
        slug = slug.replaceAll("[đ]", "d");
        slug = slug.replaceAll("[^a-zA-Z0-9\\s]", "");
        slug = slug.replaceAll("\\s+", "-");
        return slug;
    }
}
