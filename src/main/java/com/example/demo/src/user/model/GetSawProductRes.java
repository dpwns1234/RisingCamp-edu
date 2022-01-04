package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSawProductRes {
    private String title;
    private int price;
    private String deliveryType;
    private String image;
    private int starRating;
    private int reviewNum;
}
