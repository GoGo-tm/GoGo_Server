package com.tm.gogo.domain.oauth;

import com.tm.gogo.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthInfo {
    private final String nickname;
    private final String email;
    private final Member.Type type;
    private final Member.Authority authority;
}
