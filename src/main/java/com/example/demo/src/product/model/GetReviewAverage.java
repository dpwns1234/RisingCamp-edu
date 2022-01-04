package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// starRating비율, reviewNum, satisfaction비율, optionSatisfaction비율, images는 어케하지?
@Getter
@Setter
@AllArgsConstructor
public class GetReviewAverage {
    private int starRatingAverage;
    private int reviewNum;
    private int satisfactionAverage;
    private int optionSatisfactionAverage;
    private List<String> images;
}
