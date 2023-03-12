package com.tm.gogo.domain.oauth;

import com.tm.gogo.web.auth.TokenResponse;
import com.tm.gogo.web.oauth.OauthLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final OauthMemberService oauthMemberService;
    private final RequestOauthService requestOauthService;

    public TokenResponse login(OauthLoginRequest oauthLoginRequest) {
        OauthInfo oauthInfo = requestOauthService.request(oauthLoginRequest);
        return oauthMemberService.getAccessTokenWithOauthInfo(oauthInfo);
    }
}
