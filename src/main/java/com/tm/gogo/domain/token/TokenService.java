package com.tm.gogo.domain.token;

public interface TokenService {
    public Token issueToken(String value);
    public Token findToken(String key, Token.Type type);
}
