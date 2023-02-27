package com.tm.gogo.web.oauth;

import org.springframework.util.MultiValueMap;

public interface OauthLoginRequest {
    MultiValueMap<String, String> makeBody();
}
