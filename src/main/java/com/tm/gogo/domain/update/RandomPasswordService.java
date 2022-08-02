package com.tm.gogo.domain.update;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Service
public class RandomPasswordService {
    public String getRandomPassword() {
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
