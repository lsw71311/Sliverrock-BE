package com.example.silverrock.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedMatchingRes {
    private Long matchingId;  //받은 매칭의 고유 id
    private String gender; // sender의 성별
    private String nickname; // sender의 닉네임
    private String birth; // sender의 생년
    private String region; // sender의 지역(시/군/구)
    private String introduce; // sender의 한줄소개
    private GetS3Res getS3Res;
}
