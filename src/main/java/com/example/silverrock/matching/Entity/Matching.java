package com.example.silverrock.matching.Entity;

import com.example.silverrock.global.BaseTimeEntity;
import com.example.silverrock.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)// AUTO_INCREMENT 사용(순차적생성)
    private Long matchingId; // 매칭 고유번호

    @ManyToOne // @Column 어노테이션 제거
    @JoinColumn(name="sender_id", nullable = false) // 실제 데이터베이스 컬럼명 설정
    private User sender;

    @ManyToOne // @Column 어노테이션 제거
    @JoinColumn(name="receiver_id", nullable = false) // 실제 데이터베이스 컬럼명 설정
    private User receiver;

    @Column(nullable = true)
    private boolean success; // 매칭 성공여부 t/f

    @Builder
    public Matching(User sender, User receiver, Boolean success) {
        this.sender = sender;
        this.receiver = receiver;
        this.success=success;

    }

}

