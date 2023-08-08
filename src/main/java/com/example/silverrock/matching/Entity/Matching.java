package com.example.silverrock.matching.Entity;

import com.example.silverrock.global.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Matching extends BaseTimeEntity {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchingId; // 매칭 고유번호

    @Column(nullable = false)
    private Long sender; // 매칭 요청 발신자

    @Column(nullable = false)
    private Long receiver; // 매칭 요청 수신자

    @Column(nullable = true)
    private boolean success; // 매칭 성공여부 t/f

    @Builder
    public Matching(Long sender, Long receiver, Boolean success) {
        this.sender = sender;
        this.receiver = receiver;
        this.success=success;
    }


}

