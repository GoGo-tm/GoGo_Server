package com.tm.gogo.domain.oauth.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.oauth.OauthProfileResponse;
import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.helper.client.OauthApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoOauthService {

    private final OauthApiClient kakaoApiClient;

    public OauthInfo getKakaoInfo(String grantType, String clientId, String authorizationCode) throws JsonProcessingException {
        String accessToken = kakaoApiClient.getOauthAccessToken(grantType, clientId, authorizationCode);
        OauthProfileResponse oauthProfile = kakaoApiClient.getOauthProfile(accessToken);

        return OauthInfo.builder()
                .email(oauthProfile.getEmail())
                .nickname(oauthProfile.getNickName())
                .type(Member.Type.KAKAO)
                .authority(Member.Authority.ROLE_MEMBER)
                .build();
    }
}
