package com.example.silverrock.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostMatcingReq {
    private Long sender; // 매칭 요청 발신자
    private Long receiver;// 매칭 요청 수신자
}
