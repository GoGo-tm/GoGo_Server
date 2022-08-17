package com.tm.gogo.domain.token;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.helper.MailService;
import com.tm.gogo.helper.RandomPasswordGenerator;
import com.tm.gogo.web.auth.UpdateTokenDto;
import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.tm.gogo.web.response.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class NewPasswordTokenService implements TokenService{
    private final TokenRepository tokenRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

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
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 키 값입니다."));
    }

    public UpdateTokenDto issueTokenDto(String email) {
        Token token = issueToken(email);
        tokenRepository.save(token);
        return UpdateTokenDto.of(token);
    }

    public void updatePasswordAndSendMail(UpdateTokenDto updateTokenDto) {
        String txId = updateTokenDto.getTxId();
        Token.Type type = updateTokenDto.getType();

        String email = validationToken(txId, type);
        validationEmail(updateTokenDto.getEmail(), email);

        String newPassword = RandomPasswordGenerator.generate();
        updatePassword(email, newPassword);
        mailService.sendNewPassword(email, newPassword);
    }

    public String validationToken(String txId, Token.Type type) {
        Token token = findToken(txId, type);
        token.validateExpiredAt();
        return token.getValue();
    }

    public void updatePassword(String email, String newPassword) {
        Member member = findByEmail(email);
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. email: " + email));
    }

    public void validationEmail(String dtoEmail, String email) {
        if(!dtoEmail.equals(email)){
            throw new ApiException(ErrorCode.INVALID_TOKEN_VALUE, "토큰에서 제공 된 이메일과 다릅니다. dtoEmail: " + dtoEmail);
        }
    }
}
