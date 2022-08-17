package com.tm.gogo.helper;

import com.tm.gogo.web.auth.MailDto;
import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendNewPassword(String email, String newPassword) {
        MailDto mailDto = MailDto.builder()
                .address(email)
                .title("[GOGO] 고고 임시비밀번호가 생성되었습니다")
                .contents("안녕하세요. 등산 서비스 어플 GOGO 입니다.<br>" +
                        "<br>" +
                        "GOGO서비스 로그인을 위한 임시비밀번호가 발급되었습니다." +
                        "<br><br><br>" +
                        "<b>임시비밀번호 : " + newPassword + "</b>" +
                        "<br><br><br>" +
                        "발급된 임시비밀번호를 확인 후,<br>" +
                        "<br>" +
                        "즉시 GOGO어플에 로그인하여 반드시 비밀번호 변경을 해주시기 바랍니다.<br>" +
                        "<br>" +
                        "&nbsp;( 비밀번호변경방법 : 내정보 -> 내정보수정 )")
                .build();

        send(mailDto);
    }

    private void send(MailDto mailDto) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setTo(mailDto.getAddress());
            mimeMessageHelper.setSubject(mailDto.getTitle());
            mimeMessageHelper.setText(mailDto.getContents(), true);
        } catch (MessagingException e) {
            throw new ApiException(ErrorCode.REQUEST_TIMEOUT, "메세지 생성 요청시간이 초과되었습니다.");
        }

        mailSender.send(mimeMessage);
    }
}
