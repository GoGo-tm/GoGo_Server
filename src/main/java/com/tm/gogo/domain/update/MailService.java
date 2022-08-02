package com.tm.gogo.domain.update;

import com.tm.gogo.web.auth.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSenderImpl mailSender;

    public void mailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContents());
        mailSender.send(message);
    }

    //Todo: 기획한테 메일 형식 물어보기
    public MailDto creatMail(String email, String password){
        return MailDto.builder()
                .address(email)
                .title("GoGo: 임시 비밀번호 발급")
                .contents("임시 비밀번호: " + password)
                .build();
    }

}
