package com.tm.gogo.domain.oauth;

public interface OauthApiClient {
    String getOauthAccessToken(String grantType, String clientId, String authorizationCode);
    OauthProfileResponse getOauthProfile(String accessToken);
}
