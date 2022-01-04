package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// user.email, inquiry.createdAt, content ( 나중에 seller 도 만들어주기 셀러 테이블 데이터가 없어서, 그리고 jwt도 없어서 만들려면 오래 걸릴 것 같음 )
@Getter
@Setter
@AllArgsConstructor
public class GetInquiryRes {
    private String userEmail;
    private String inquiryCreatedAt;
    private String userContent;
}
