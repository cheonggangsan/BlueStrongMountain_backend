package com.ssafy.BlueStrongMountain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendTemporaryPassword(String toEmail, String tempPassword){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[BlueStrongMountain] 임시 비밀번호 안내");
        message.setText(
                "임시 비밀번호가 발급되었습니다.\n\n"
                        + "임시 비밀번호: " + tempPassword + "\n\n"
                        + "로그인 후 반드시 비밀번호를 변경해주세요."
        );
        mailSender.send(message);
    }
}
