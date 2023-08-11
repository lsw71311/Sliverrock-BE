package com.example.silverrock.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostMatcingReq {
    private Long sender; // 매칭 요청 발신자
    private Long receiver;
}
