package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// title, image, starRating, review.createdAt, content, satisfaction, optionSatisfaction
@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {
    private String title;
    private String image;
    private int starRating;
    private String createdAt; // 리뷰가 작성된 날
    private String content;
    private String satisfaction;
    private String optionSatisfaction;
}
