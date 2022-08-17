package com.tm.gogo.service;

import com.tm.gogo.domain.token.NewPasswordToken;
import com.tm.gogo.domain.token.NewPasswordTokenService;
import com.tm.gogo.domain.token.Token;
import com.tm.gogo.domain.token.TokenRepository;
import com.tm.gogo.web.auth.UpdateTokenDto;
import com.tm.gogo.web.response.ApiException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class NewPasswordTokenServiceTest {

    @Autowired
    private NewPasswordTokenService newPasswordTokenService;

    @Autowired
    private TokenRepository tokenRepository;

    @Nested
    @DisplayName("새로운 비밀번호 토큰 테스트")
    class newPasswordTest {

        @Test
        @DisplayName("비밀번호 변경 토큰 검증 성공")
        void testValidationToken() {
            //given
            String email = "dustle@gmail.com";
            Token issueToken = NewPasswordToken.builder()
                    .key(RandomStringUtils.randomAlphanumeric(10))//랜덤 키 값
                    .value(email)
                    .expiredAt(LocalDateTime.now().plusSeconds(10))
                    .build();

            tokenRepository.saveAndFlush(issueToken);
            UpdateTokenDto tokenDto = UpdateTokenDto.of(issueToken);

            //when
            newPasswordTokenService.validateToken(tokenDto.getTxId(), email);
        }

        @Test
        @DisplayName("비밀번호 변경 토큰 만료 기간 지나서 검증 실패")
        void testFailValidationToken() {
            //given
            String email = "dustle@gmail.com";
            Token issueToken = NewPasswordToken.builder()
                    .key(RandomStringUtils.randomAlphanumeric(10))//랜덤 키 값
                    .value(email)
                    .expiredAt(LocalDateTime.now().minusMinutes(10))
                    .build();

            tokenRepository.saveAndFlush(issueToken);
            UpdateTokenDto tokenDto = UpdateTokenDto.of(issueToken);

            //then
            assertThatExceptionOfType(ApiException.class)
                    .isThrownBy(() -> newPasswordTokenService.validateToken(tokenDto.getTxId(), email))
                    .withMessage("토큰 만료 기간이 지났습니다. txId: " + tokenDto.getTxId());
        }
    }
}
