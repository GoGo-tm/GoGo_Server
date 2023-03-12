package com.tm.gogo.domain.oauth;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.web.oauth.OauthLoginRequest;

public interface OauthApiClient {
    String getOauthAccessToken(OauthLoginRequest oauthLoginRequest);

    OauthProfileResponse getOauthProfile(String accessToken);

    Member.Type getMemberType();
}
