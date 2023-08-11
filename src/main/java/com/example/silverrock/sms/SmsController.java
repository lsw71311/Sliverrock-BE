package com.example.silverrock.sms;
import com.example.silverrock.global.Response.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/sms/send")
    public SmsRes sendSms(@RequestBody MessageDto messageDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        SmsRes responseDto = smsService.sendSms(messageDto);
        return responseDto;
    }

    @PostMapping("/verify")
    public BaseResponse<String> verifySmsCode(@RequestParam(name = "key") String key) throws ChangeSetPersister.NotFoundException {
        boolean isCodeValid = smsService.verifyEmail(key);

        if (isCodeValid) {
            return new BaseResponse("인증 번호 확인 성공");
        } else {
            return new BaseResponse("인증 번호 확인 실패");
        }
    }


}