package com.tm.gogo.domain.oauth.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.helper.client.KakaoApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoOauthService {

    private final KakaoApiClient kakaoApiClient;

    public OauthInfo getKakaoInfo(String grantType, String clientId, String authorizationCode) throws JsonProcessingException {
        String kakaoAccessToken = kakaoApiClient.kakaoLogin(grantType, clientId, authorizationCode);
        KakaoMyInfo kakaoMyInfo = kakaoApiClient.getKakaoInfo(kakaoAccessToken);

        return OauthInfo.builder()
                .email(kakaoMyInfo.getKakaoAccount().getEmail())
                .nickname(kakaoMyInfo.getKakaoAccount().getProfile().getNickname())
                .type(Member.Type.KAKAO)
                .authority(Member.Authority.ROLE_MEMBER)
                .build();
    }
}
