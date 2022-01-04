package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBasketRes {
    private String title;
    private String image;
    private int price;
    private String deliveryType;
    private String arrivalTime;
}
