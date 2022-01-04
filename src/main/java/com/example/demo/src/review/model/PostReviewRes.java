package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewRes {
    private int starRating;
    private String content;
    private String satisfaction;
    private String optionSatisfaction;
}
