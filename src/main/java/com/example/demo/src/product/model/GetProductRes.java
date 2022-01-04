package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 제목, 내용, 이미지(여러 개), 가격, 배송 타입, 판매량, 남은 개수, 도착 정보, 별점, 리뷰 개수, 판매자 이름 조회
@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {
    private String title;
    private String content;
    private int price;
    private String deliveryType;
    private int saleVolume;
    private int remainNum;
    private String arrivalTime;
    private int starRating;
    private int reviewNum;
    private String sellerName;

}
