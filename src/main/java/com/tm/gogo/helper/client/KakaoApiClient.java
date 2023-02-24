package com.tm.gogo.helper.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.gogo.domain.oauth.kakao.KakaoMyInfo;
import com.tm.gogo.domain.oauth.kakao.KakaoToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoApiClient {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String kakaoLogin(String grantType, String clientId, String authorizationCode) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("code", authorizationCode);

        String url = "https://kauth.kakao.com/oauth/token";
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(url, request, String.class);
        KakaoToken kakaoToken = OBJECT_MAPPER.readValue(response.getBody(), KakaoToken.class);

        return kakaoToken.getAccessToken();
    }

    public KakaoMyInfo getKakaoInfo(String kakaoAccessToken) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + kakaoAccessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        String url = "https://kapi.kakao.com/v2/user/me";
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(url, request, String.class);

        return OBJECT_MAPPER.readValue(response.getBody(), KakaoMyInfo.class);
    }
}
