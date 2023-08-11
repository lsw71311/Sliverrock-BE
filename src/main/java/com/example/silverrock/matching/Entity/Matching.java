package com.example.silverrock.matching.Entity;

import com.example.silverrock.global.BaseTimeEntity;
import com.example.silverrock.user.User;
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

    @ManyToOne()
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // 매칭 요청 발신자

    @ManyToOne()
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // 매칭 요청 수신자

    @Column(nullable = true)
    private boolean success; // 매칭 성공여부 t/f

    @Builder
    public Matching(User sender, User receiver, Boolean success) {
        this.sender = sender;
        this.receiver = receiver;
        this.success=success;
    }


}

