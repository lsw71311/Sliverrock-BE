package com.example.silverrock.user;

import com.example.silverrock.global.BaseTimeEntity;
import com.example.silverrock.login.jwt.Token;
import com.example.silverrock.matching.Entity.Matching;
import com.example.silverrock.user.profile.Profile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 멤버의 식별자

    @Column(nullable = false)
    private String phoneNum; // 유저의 휴대폰번호

    @Column(nullable = false)
    private String gender; // 유저의 성별

    @Column(nullable = false)
    private String nickname; // 유저의 닉네임

    @Column(nullable = false)
    private String birth; // 유저의 생년

    @Column(nullable = false)
    private String region; // 유저의 지역(시/군/구)

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String introduce; // 한줄소개

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Token token; // 토큰과 일대일 매핑

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile; // 프로필 사진과 일대일 매핑

    @OneToMany(
            mappedBy = "sender",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @JsonBackReference
    private List<Matching> sentMatchings;

    @OneToMany(
            mappedBy = "receiver",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @JsonBackReference
    private List<Matching> receivedMatchings;

    @Builder
    public User(String phoneNum, String gender, String nickname, String birth, String region, String password, String introduce) {
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.nickname = nickname;
        this.birth = birth;
        this.region = region;
        this.password = password;
        this.introduce = introduce;
    }


}
