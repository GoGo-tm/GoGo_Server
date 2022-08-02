package com.tm.gogo.helper;

import com.tm.gogo.web.auth.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendNewPassword(String email, String newPassword) {
        //Todo: 기획한테 메일 형식 물어보기
        MailDto mailDto = MailDto.builder()
                .address(email)
                .title("GoGo: 임시 비밀번호 발급")
                .contents("임시 비밀번호: " + newPassword)
                .build();

        send(mailDto);
    }

    private void send(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContents());
        mailSender.send(message);
    }
}
