package com.tm.gogo.domain.token;

public interface TokenService {
    Token issueToken(String key, String value);
    Token findToken(String key);
}
