package com.tm.gogo.domain.oauth.google;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.oauth.OauthApiClient;
import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.domain.oauth.OauthProfileResponse;
import com.tm.gogo.web.oauth.GoogleLoginRequest;
import com.tm.gogo.web.oauth.NaverLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOauthService {
    private final OauthApiClient googleApiClient;

    public OauthInfo getGoogleInfo(GoogleLoginRequest googleLoginRequest) {
        String accessToken = googleApiClient.getOauthAccessToken(googleLoginRequest);
        OauthProfileResponse oauthProfile = googleApiClient.getOauthProfile(accessToken);

        return OauthInfo.builder()
                .email(oauthProfile.getEmail())
                .nickname(oauthProfile.getNickName())
                .type(Member.Type.GOOGLE)
                .authority(Member.Authority.ROLE_MEMBER)
                .build();
    }
}
