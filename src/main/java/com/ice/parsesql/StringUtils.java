package com.ice.parsesql;

public class StringUtils {

    private StringUtils() {}

    public static String removeNameQuotes(String str) {
        if (str != null && str.length() > 1) {
            int len = str.length();
            char c0 = str.charAt(0);
            char last = str.charAt(len - 1);
            return c0 != last && c0 != '[' || c0 != '`' && c0 != '\'' && c0 != '"' && c0 != '[' ? str : str.substring(1, len - 1);
        }
        return null;
    }
}
