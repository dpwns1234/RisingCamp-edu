package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


// starRating비율, reviewNum, images, satisfaction비율, optionSatisfaction비율 -> 이건 종합적인 거라 만들어야 할 듯
// userName, starRating, product.title, content, helpNum, satisfaction, optionSatisfaction
// images는 여러개이므로 따로 쿼리문 만들어서 보내줘야 할 듯.
@Getter
@Setter
@AllArgsConstructor
public class GetProductReview {
    private String userName;
    private int starRating;
    private String productTitle;
    private String content;
    private int helpNum;
    private String satisfaction;
    private String optionSatisfaction;
    private List<String> images;
    //private int reviewIdx; // 잘 돌아가면 빼고 돌려보자 (아마 안 돌아갈듯) -> 결과: 들어가면 에러 ㅋㅋ
}
