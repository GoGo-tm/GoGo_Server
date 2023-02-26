package com.tm.gogo.domain.oauth.kakao;

import com.tm.gogo.domain.oauth.OauthApiClient;
import com.tm.gogo.domain.oauth.OauthProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OauthApiClient {

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Override
    public String getOauthAccessToken(String grantType, String clientId, String authorizationCode) {
        String url = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = newHttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("code", authorizationCode);
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<KakaoToken> response = restTemplate.postForEntity(url, request, KakaoToken.class);

        return Objects.requireNonNull(response.getBody()).getAccessToken();
    }

    @Override
    public OauthProfileResponse getOauthProfile(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = newHttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<KakaoMyInfo> response = restTemplate.postForEntity(url, request, KakaoMyInfo.class);

        return response.getBody();
    }

    private HttpHeaders newHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }
}
