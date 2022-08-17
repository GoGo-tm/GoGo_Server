package com.tm.gogo.domain.token;

import com.tm.gogo.domain.jwt.TokenProvider;
import com.tm.gogo.web.auth.TokenRequest;
import com.tm.gogo.web.auth.TokenResponse;
import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService implements TokenService{
    private final TokenRepository tokenRepository;

    private final TokenProvider tokenProvider;

    //Todo:메소드 사용처 현재 클래스에서 없음
    @Override
    public Token issueToken(String value) {
        Token issueToken = Token.builder()
                .key(RandomStringUtils.randomAlphanumeric(10))//랜덤 키 값
                .value(value)
                .expiredAt(LocalDateTime.now().plusSeconds(10))
                .type(Token.Type.NEW_PASSWORD)
                .build();

        tokenRepository.save(issueToken);
        return issueToken;
    }

    @Override
    public Token findToken(String key, Token.Type type) {
        return tokenRepository.findByKeyAndType(key, type)
                .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED_REFRESH_TOKEN, "로그아웃 된 사용자입니다."));
    }

    @Transactional
    public TokenResponse reissue(TokenRequest tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN, "Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        Token refreshToken = findToken(authentication.getName(), Token.Type.REFRESH);

        if (refreshToken.isNotEqualTo(tokenRequestDto.getRefreshToken())) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_NOT_MATCH, "토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenResponse tokenDto = tokenProvider.generateTokenDto(authentication);

        refreshToken.renewValue(tokenDto.getRefreshToken(), LocalDateTime.now().plusWeeks(2));

        return tokenDto;
    }


}
