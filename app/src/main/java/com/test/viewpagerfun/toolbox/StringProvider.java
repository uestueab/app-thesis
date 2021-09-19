package com.test.viewpagerfun.toolbox;

public class StringProvider {

    /**
     * @param str string
     * @return string without any spaces and transformed to lowercase.
     */
    public static String toComparable(String str) {
        String noSpaceString = stripAllWhiteSpaces(str);
        return noSpaceString.toLowerCase();
    }

    //removes ALL white spaces from a string using regex.
    public static String stripAllWhiteSpaces(String str) {
        return str.replaceAll("\\s+", "");
    }

    /**
     *
     * @param str string to be formatted
     * @return " hello   world  " becomes "hello world"
     */
    public static String omitWhiteSpace(String str) {
        return str.trim().replaceAll(" +", " ");
    }
}
