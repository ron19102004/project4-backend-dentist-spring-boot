package com.hospital.infrastructure.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class GgImageUtil {
    public String parse(String path){
        final String regex = "(?:/file/d/)([a-zA-Z0-9_-]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return "https://lh3.googleusercontent.com/d/" + matcher.group(1);
        }
        return path;
    }
}
