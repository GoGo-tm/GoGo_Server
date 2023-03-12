package com.tm.gogo.web.oauth;

import com.tm.gogo.domain.member.Member;
import org.springframework.util.MultiValueMap;

public interface OauthLoginRequest {
    MultiValueMap<String, String> makeBody();

    Member.Type memberType();
}
