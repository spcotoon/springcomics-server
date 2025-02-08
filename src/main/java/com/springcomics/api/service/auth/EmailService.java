package com.springcomics.api.service.auth;

import com.springcomics.api.exception.InvalidEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("스프링코믹스 회원가입 인증코드");
        message.setText("인증 코드: " + otp);

        try {
            mailSender.send(message);
        } catch (MailSendException e) {
            throw new InvalidEmail();
        }
    }
}
