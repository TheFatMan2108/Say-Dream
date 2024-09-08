package com.thuydev.saydream.Extentions;

import java.util.regex.Pattern;

public class Regex {
    public static String CheckEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static String CheckPhone = "^0\\d{9,10}$";

    public static boolean CheckRegex(String regex,String input) {
        return !Pattern.compile(regex).matcher(input).matches();
    }
}
