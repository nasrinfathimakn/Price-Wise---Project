package com.pricewise.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ProductNormalizer {

    private static final Pattern STORAGE_PATTERN = Pattern.compile("(?i)\\b(\\d{2,4})\\s*(gb|tb)\\b");
    private static final Pattern RAM_PATTERN = Pattern.compile("(?i)\\b(\\d{1,2})\\s*(gb)\\s*(ram)?\\b");

    private ProductNormalizer() {
    }

    public static String normalizeBrand(String brand) {
        if (brand == null) return "";
        String clean = brand.trim().toLowerCase(Locale.ROOT);
        if (clean.contains("apple")) return "Apple";
        if (clean.contains("samsung")) return "Samsung";
        if (clean.contains("sony")) return "Sony";
        if (clean.contains("hp") || clean.contains("hewlett")) return "HP";
        if (clean.contains("boat")) return "boAt";
        if (clean.contains("oneplus")) return "OnePlus";
        if (clean.contains("dell")) return "Dell";
        return brand.trim();
    }

    public static String extractStorage(String text) {
        if (text == null) return null;
        Matcher matcher = STORAGE_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1) + matcher.group(2).toUpperCase(Locale.ROOT);
        }
        return null;
    }

    public static String extractRam(String text) {
        if (text == null) return null;
        Matcher matcher = RAM_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1) + "GB";
        }
        return null;
    }

    public static double calculateSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        String str1 = s1.toLowerCase(Locale.ROOT).trim();
        String str2 = s2.toLowerCase(Locale.ROOT).trim();
        if (str1.equals(str2)) return 1.0;

        int distance = levenshteinDistance(str1, str2);
        int maxLen = Math.max(str1.length(), str2.length());
        if (maxLen == 0) return 1.0;

        return 1.0 - ((double) distance / maxLen);
    }

    private static int levenshteinDistance(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}

