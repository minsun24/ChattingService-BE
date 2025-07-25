package com.example.chatserver.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder   //
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // GeneratedValue(= AutoIncrement 설정), IDENTITY(자동 값 증가 직접 지정)
    private Long id;

    private String name;

    @Column(nullable=false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

}


/*
String 값 기본 VARCAHR(255)

데이터베이스에 그냥 Enum 값을 집어 넣으면 Enum 순서에 따른 숫자값으로 들어감.
Enum String 값 자체를 넣으려면 @Enumerated(EnumType.STRING) 설정 필요

@Builder 는 생성자 필요
 */