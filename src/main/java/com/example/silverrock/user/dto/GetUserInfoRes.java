package com.example.silverrock.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserInfoRes {
    private String phoneNum; // 유저의 휴대폰번호
    private String gender; // 유저의 성별
    private String nickname; // 유저의 닉네임
    private String birth; // 유저의 생년
    private String region; // 유저의 지역(시/군/구)
    private String introduce; // 한줄소개
    private GetS3Res getS3Res;
}
