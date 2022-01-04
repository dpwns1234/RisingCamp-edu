package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//제목, 이미지(하나), 가격, 배송 타입, 도착 정보, 별점, 리뷰 수(세일 전 가격, 쿠팡 추천 이것들은 나중에 디테일로) 조회
@Getter
@Setter
@AllArgsConstructor
public class GetProductCategoryRes {
    private String categoryName;
    private String title;
    private String image;
    private int price;
    private String deliveryType;
    private String arrivalTime;
    private int starRating;
    private int reviewNum;
}
