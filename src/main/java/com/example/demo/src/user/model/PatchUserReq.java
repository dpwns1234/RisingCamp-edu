package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserReq {
    private int userIdx;
    private String id;
    private String name;
    private String password;
    private String email;
    private String status;
    private String address;
    private String phoneNum;

    // 광고 동의 동의하면 Agree, 동의하지 않으면 Disagree
    private String agreeAdEmail;
    private String agreeAdPhone;
    private String agreeAdPush;
}
