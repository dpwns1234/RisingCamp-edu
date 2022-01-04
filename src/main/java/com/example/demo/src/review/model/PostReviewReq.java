package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewReq {
    private int starRating;
    private String content;
    private String satisfaction;
    private String optionSatisfaction;

    // 제대로 된 접근인지 확인하기 위해 서버에서 set 해줄 변수들
    private int userIdx;
    private int productIdx;
}
