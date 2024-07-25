package com.finalproject.airport.member.service;

import com.finalproject.airport.store.dto.AuthMailPhoneDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendForAuthCode(AuthMailPhoneDTO authMailDTO) throws MessagingException {
        int authCode = authMailDTO.getAuthCode();
        if (99999 < authCode && authCode <= 999999) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String content = "<h2>AirService 인증코드입니다.</h2>" +
                    "<br> <br>" +
                    "<h3>인증코드는 " + authCode + "입니다.<h3>" +
                    "<a href=\"http://localhost:5173/auth/certification\">회원가입 하기</a>";

            helper.setTo(authMailDTO.getUserEmail());
            helper.setSubject("AirService 인증코드입니다.");
            helper.setText(content, true);

            mailSender.send(message);
        }
    }


    public void sendForNewPassword(String userEmail, String randomCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String content = "<h2>AirService 임시비밀번호 입니다.</h2>" +
                "<br> <br>" +
                "<h3>임시 비밀번호는 " + randomCode + "입니다.</h3>" +
                "<br> <br>" +
                "<h3> 반드시 비밀번호를 변경해주세요 </h3> " +
                "<br> <br>" +
                "<a href=\"http://localhost:5173/auth/loginformik\">로그인 하기</a>";

        helper.setTo(userEmail);
        helper.setSubject("AirService 임시비밀번호 입니다.");
        helper.setText(content, true);

        mailSender.send(message);
    }
}
