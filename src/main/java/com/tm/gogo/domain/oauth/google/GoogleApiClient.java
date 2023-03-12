package com.tm.gogo.domain.oauth.google;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.oauth.OauthApiClient;
import com.tm.gogo.domain.oauth.OauthProfileResponse;
import com.tm.gogo.web.oauth.OauthLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GoogleApiClient implements OauthApiClient {

    @Value("${oauth.google.url.auth}")
    private String authUrl;

    @Value("${oauth.google.url.api}")
    private String apiUrl;

    @Value("${oauth.google.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Override
    public String getOauthAccessToken(OauthLoginRequest oauthLoginRequest) {
        String url = authUrl + "/token";

        HttpHeaders httpHeaders = newHttpHeaders();

        MultiValueMap<String, String> body = oauthLoginRequest.makeBody();
        body.add("client_secret", clientSecret);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<GoogleToken> response = restTemplate.postForEntity(url, request, GoogleToken.class);

        return Objects.requireNonNull(response.getBody()).getAccessToken();
    }

    @Override
    public OauthProfileResponse getOauthProfile(String accessToken) {
        String url = apiUrl + "/v1/people/me?personFields=nicknames,emailAddresses,names";

        HttpHeaders httpHeaders = newHttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<GoogleMyInfo> response = restTemplate.exchange(url, HttpMethod.GET, request, GoogleMyInfo.class);

        return response.getBody();
    }

    @Override
    public Member.Type getMemberType() {
        return Member.Type.GOOGLE;
    }

    private HttpHeaders newHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }
}
