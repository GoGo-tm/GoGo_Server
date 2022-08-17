package com.tm.gogo.helper;

import java.security.SecureRandom;

public class RandomPasswordGenerator {

    public static String generate() {
        int len = 10;
        int randNumOrigin = 48;
        int randNumBound = 122;

        SecureRandom random = new SecureRandom();
        return random.ints(randNumOrigin, randNumBound + 1)
                .filter(i -> Character.isAlphabetic(i) || Character.isDigit(i))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }
}
