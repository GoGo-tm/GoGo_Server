package com.tm.gogo.domain.token;

import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class NewPasswordTokenService implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Token issueToken(String key, String value) {
        Token issueToken = NewPasswordToken.builder()
                .key(key)
                .value(value)
                .expiredAt(LocalDateTime.now().plusSeconds(10))
                .build();

        tokenRepository.save(issueToken);
        return issueToken;
    }

    @Override
    public Token findToken(String key) {
        return tokenRepository.findFirstByKeyOrderByIdDesc(key)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 키 값입니다."));
    }

    public void validateToken(String txId, String value) {
        Token token = findToken(txId);
        token.validateExpiredAt();
        token.validateValue(value);
    }
}
