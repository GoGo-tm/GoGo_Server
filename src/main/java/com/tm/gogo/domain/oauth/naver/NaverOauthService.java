package com.tm.gogo.domain.oauth.naver;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.oauth.OauthApiClient;
import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.domain.oauth.OauthProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverOauthService {

    private final OauthApiClient naverApiClient;

    public OauthInfo getNaverInfo(String grantType, String clientId, String code, String state) {
        String accessToken = naverApiClient.getOauthAccessToken(grantType, clientId, code, state);
        OauthProfileResponse oauthProfile = naverApiClient.getOauthProfile(accessToken);

        return OauthInfo.builder()
                .email(oauthProfile.getEmail())
                .nickname(oauthProfile.getNickName())
                .type(Member.Type.NAVER)
                .authority(Member.Authority.ROLE_MEMBER)
                .build();
    }
}
