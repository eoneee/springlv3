package com.example.springlv3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@NoArgsConstructor
@Entity(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    //userId라고 칭할 것
    private Long id;

    @Column(nullable = false, unique = true)
    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용) true : 중복 안됨
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    //json에서 출력되지 않음
    private String password;

    @Column(nullable = false)
    //null은 안됨
    @Enumerated(value = EnumType.STRING)
    //Enum의 선언된 상수의 이름을 string으로 변환하여 DB에 주입
    private UserRoleEnum role;

    public Users(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }



}