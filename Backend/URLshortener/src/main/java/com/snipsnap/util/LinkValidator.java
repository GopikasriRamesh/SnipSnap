package com.snipsnap.util;

import java.util.Arrays;
import java.util.List;

public class LinkValidator {

    private static final List<String> blockedPatterns = Arrays.asList(
            "porn", "hack", "phish", "malware", "free-money", "bit.ly", ".ru", ".xyz"
    );

    public static boolean isMalicious(String url) {
        String lowerUrl = url.toLowerCase();

        for (String pattern : blockedPatterns) {
            if (lowerUrl.contains(pattern)) {
                return true;
            }
        }

        return false;
    }
}
