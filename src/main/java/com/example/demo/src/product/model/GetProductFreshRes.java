package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//title, price, deliveryType, arrivalTime, category, starRating, reviewNum
@Getter
@Setter
@AllArgsConstructor
public class GetProductFreshRes {
    private String title;
    private String image;
    private int price;
    private String deliveryType;
    private String arrivalTime;
    private String category;
    private int starRating;
    private int reviewNum;

}
