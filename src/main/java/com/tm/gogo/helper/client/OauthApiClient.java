package com.tm.gogo.helper.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tm.gogo.domain.oauth.OauthProfileResponse;

public interface OauthApiClient {
    String getOauthAccessToken(String grantType, String clientId, String authorizationCode) throws JsonProcessingException;

    OauthProfileResponse getOauthProfile(String accessToken) throws JsonProcessingException;
}
