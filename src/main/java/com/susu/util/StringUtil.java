package com.susu.util;

/**
 * @Date:2023/8/15 14:37
 * @Created by Muqing
 */
public class StringUtil {
    public static String extractValue(String input, String key) {
        int startIndex = input.indexOf(key);
        if (startIndex == -1) {
            return null;
        }
        startIndex += key.length();

        int endIndex = input.indexOf(',', startIndex);
        if (endIndex == -1) {
            endIndex = input.length() - 1;
        }

        return input.substring(startIndex, endIndex).trim();
    }
}
