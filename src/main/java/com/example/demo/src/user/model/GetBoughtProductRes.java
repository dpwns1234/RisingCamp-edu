package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 제목, 이미지, 배달타입, 가격, 주문한 날, 배송 중인지 배송 완료인지, 구매한 개수, 배송 완료 날짜
@Getter
@Setter
@AllArgsConstructor
public class GetBoughtProductRes {
    private String title;
    private String image;
    private String deliveryType;
    private int price;
    private String status; // deliveryStatus, 배달중인지, 배달완료인지를 나타내는 status
    private int buyNum;
    private String boughtAt;
    private String deliveryDay;
}
