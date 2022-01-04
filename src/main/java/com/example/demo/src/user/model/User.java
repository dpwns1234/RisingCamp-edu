package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 데이터가 이렇게 많이 필요할까?
@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userIdx;
    private String id;
    private String name;
    private String password;
    private String email;
    private String status;
}
