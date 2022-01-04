package com.example.demo.src.review;


import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ReviewService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;


    @Autowired
    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;

    }


    //POST
    public PostReviewRes postReview(PostReviewReq postReviewReq) throws BaseException {
    //    try {
            PostReviewRes postReviewRes = new PostReviewRes(postReviewReq.getStarRating(), postReviewReq.getContent(),
                    postReviewReq.getSatisfaction(), postReviewReq.getOptionSatisfaction());
            int userIdx = postReviewReq.getUserIdx();
            int productIdx = postReviewReq.getProductIdx();

            // 이 유저가 이미 리뷰를 작성했는지 검사 ( 작성했으면 1, 작성하지 않았다면 0 )
            if(reviewProvider.checkReviewExist(userIdx, productIdx) == 1) {
                throw new BaseException(ALREADY_REVIEW_EXIST);
            }

            // 이 유저가 이 해당 상품을 샀는지 검사 ( 샀으면 1, 사지 않았다면 0 )
            if(reviewProvider.checkBoughtProduct(userIdx, productIdx) == 0)
                throw new BaseException(NOT_BOUGHT_PRODUCT);

            // 이제 작성(Post)
            // 별점만 필수로 입력하면 되고(insert into)
            reviewDao.postReview(postReviewReq);

            //나머지는 선택이므로 (update set) 으로 해준다.
            patchReview(postReviewReq);

            return postReviewRes;
            // 왜 지워야만 잘 돌아갈까??
    //    } catch(Exception e) {
    //        e.printStackTrace();
    //        throw new BaseException(DATABASE_ERROR);
    //    }
    }

    public void patchReview(PostReviewReq postReviewReq) throws BaseException {
        try {
            // 별점 수정
            if(postReviewReq.getStarRating() != 0) {
                reviewDao.updateStarRating(postReviewReq);
            }
            // content가 있다면 update
            if(postReviewReq.getContent() != null) {
                reviewDao.updateContent(postReviewReq);
            }
            // satisfaction가 있다면 update
            if(postReviewReq.getSatisfaction() != null) {
                reviewDao.updateSatisfaction(postReviewReq);
            }
            // optionSatisfaction가 있다면 update
            if(postReviewReq.getOptionSatisfaction() != null) {
                reviewDao.updateOptionSatisfaction(postReviewReq);
            }

        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
