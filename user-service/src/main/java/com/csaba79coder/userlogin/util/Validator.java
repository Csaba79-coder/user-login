package com.csaba79coder.userlogin.util;

import java.util.regex.Pattern;

public class Validator {

    public static boolean patternMatches(String pattern, String regexPattern) {
        if (pattern == null) {
            return false;
        }
        return Pattern.compile(regexPattern)
                .matcher(pattern)
                .matches();
    }

    private Validator() {

    }
}
