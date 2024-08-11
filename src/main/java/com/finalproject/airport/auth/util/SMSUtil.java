package com.finalproject.airport.auth.util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMSUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey,apiSecretKey,"https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendOne(String to, int authCode) {
        String signUpUrl = "https://url.kr/u1dnsz";
        Message message = new Message();
        message.setFrom("01079410489");
        message.setTo(to);
        message.setText("[SkyCare] 회원가입 인증코드 입니다.\n [" + authCode + "]" +  signUpUrl  );

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

    public SingleMessageSentResponse newpassword(String userPhone, String randomCode) {

        Message message = new Message();
        message.setFrom("01079410489");
        message.setTo(userPhone);
        message.setText("[SkyCare] 임시 비밀번호입니다 .\n [" + randomCode + "]"  );

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }
}
