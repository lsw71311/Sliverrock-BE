package com.example.silverrock.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetNearUserRes {
    private Long id;  //유저의 고유 id
    private String gender; // 유저의 성별
    private String nickname; // 유저의 닉네임
    private String birth; // 유저의 생년
    private String region; // 유저의 지역(시/군/구)
    private String introduce; // 한줄소개
    private GetS3Res getS3Res;
}