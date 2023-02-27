package com.tm.gogo.domain.oauth.kakao;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.oauth.OauthApiClient;
import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.domain.oauth.OauthProfileResponse;
import com.tm.gogo.web.oauth.OauthLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoOauthService {
    private final OauthApiClient kakaoApiClient;

    public OauthInfo getKakaoInfo(OauthLoginRequest oauthLoginRequest) {
        String accessToken = kakaoApiClient.getOauthAccessToken(oauthLoginRequest);
        OauthProfileResponse oauthProfile = kakaoApiClient.getOauthProfile(accessToken);

        return OauthInfo.builder()
                .email(oauthProfile.getEmail())
                .nickname(oauthProfile.getNickName())
                .type(Member.Type.KAKAO)
                .authority(Member.Authority.ROLE_MEMBER)
                .build();
    }
}
