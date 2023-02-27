package com.tm.gogo.domain.oauth.naver;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.oauth.OauthApiClient;
import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.domain.oauth.OauthProfileResponse;
import com.tm.gogo.web.oauth.NaverLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverOauthService {
    private final OauthApiClient naverApiClient;

    public OauthInfo getNaverInfo(NaverLoginRequest naverLoginRequest) {
        String accessToken = naverApiClient.getOauthAccessToken(naverLoginRequest);
        OauthProfileResponse oauthProfile = naverApiClient.getOauthProfile(accessToken);

        return OauthInfo.builder()
                .email(oauthProfile.getEmail())
                .nickname(oauthProfile.getNickName())
                .type(Member.Type.NAVER)
                .authority(Member.Authority.ROLE_MEMBER)
                .build();
    }
}
