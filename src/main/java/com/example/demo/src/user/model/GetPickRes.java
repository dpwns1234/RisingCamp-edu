package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// title, price, image, deliveryType, remainNum( 품절되면 그에 맞는 값만 내보내기, status 확인, 세일 적용 )
@Getter
@Setter
@AllArgsConstructor
public class GetPickRes {
    private String title;
    private String image;
    private int price;
    private String deliveryType;
    private int remainNum;


}
