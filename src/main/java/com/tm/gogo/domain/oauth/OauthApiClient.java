package com.tm.gogo.domain.oauth;

public interface OauthApiClient {
    String getOauthAccessToken(String grantType, String clientId, String authorizationCode);

    String getOauthAccessToken(String grantType, String clientId, String code, String state);

    OauthProfileResponse getOauthProfile(String accessToken);
}
