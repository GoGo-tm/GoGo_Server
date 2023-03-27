package com.tm.gogo.domain.member;


import com.tm.gogo.domain.token.NewPasswordTokenService;
import com.tm.gogo.domain.token.Token;
import com.tm.gogo.helper.MailService;
import com.tm.gogo.helper.RandomPasswordGenerator;
import com.tm.gogo.web.auth.UpdateTokenDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangePasswordService {

    private final CommandMemberService commandMemberService;
    private final NewPasswordTokenService newPasswordTokenService;
    private final MailService mailService;

    public UpdateTokenDto issueToken(String email) {
        Token token = newPasswordTokenService.issueToken(RandomStringUtils.randomAlphanumeric(10), email);
        return UpdateTokenDto.of(token);
    }

    public void updatePasswordAndSendMail(UpdateTokenDto updateTokenDto) {
        String txId = updateTokenDto.getTxId();
        String email = updateTokenDto.getEmail();

        newPasswordTokenService.validateToken(txId, email);

        String newPassword = RandomPasswordGenerator.generate();
        commandMemberService.updatePassword(email, newPassword);
        mailService.sendNewPassword(email, newPassword);
    }
}
