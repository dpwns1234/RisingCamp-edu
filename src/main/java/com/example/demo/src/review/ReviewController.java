package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.config.BaseResponseStatus.POST_REVIEW_EMPTY_STARRATING;

@RestController
@RequestMapping("/app/review")
public class ReviewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;


    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService) {
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * 리뷰 작성 API
     * content, starRating, satisfaction, optionSatisfaction
     * [POST] /app/review/:userIdx/:productIdx
     * @return BaseResponse<PostReviewRes>
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{productIdx}")
    public BaseResponse<PostReviewRes> postReviewRes(@PathVariable("userIdx") int userIdx,
                                                     @PathVariable("productIdx") int productIdx,
                                                     @RequestBody PostReviewReq postReviewReq) {
        try{
            // 별점이 0(=null) 값이면 validation ( 클라에서 별점 주기할 때 0점은 줄 수 없도록 만들어야 함 )
            if(postReviewReq.getStarRating() == 0) {
                return new BaseResponse<>(POST_REVIEW_EMPTY_STARRATING);
            }

            // jwt으로 userIdx 추출
            int userIdxByJwt = jwtService.getUserIdx();
            // 권한없는 유저
            if(userIdxByJwt != userIdx)
                return new BaseResponse<>(INVALID_USER_JWT);

            postReviewReq.setUserIdx(userIdx);
            postReviewReq.setProductIdx(productIdx);
            PostReviewRes postReviewRes = reviewService.postReview(postReviewReq);
            return new BaseResponse<>(postReviewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내가 작성한 리뷰 조회 API
     * title, image, starRating, review.createdAt, content, satisfaction, optionSatisfaction
     * [GET] /app/review/:userIdx
     * @return BaseResponse<List<GetReviewRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetReviewRes>> getReviewRes(@PathVariable("userIdx") int userIdx) {
        try{
            List<GetReviewRes> getReviewRes = reviewProvider.getReview(userIdx);
            return new BaseResponse<>(getReviewRes);
        }
        catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 리뷰 수정 API
     * [PATCH] /app/review/:userIdx/:productIdx
     * @return String
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/{productIdx}")
    public BaseResponse<String> modifyReview(@PathVariable("userIdx") int userIdx, @PathVariable("productIdx") int productIdx,
                                             @RequestBody PostReviewReq postReviewReq) {
        try {
            // jwt으로 userIdx 추출
            int userIdxByJwt = jwtService.getUserIdx();
            // 권한없는 유저
            if (userIdxByJwt != userIdx)
                return new BaseResponse<>(INVALID_USER_JWT);

            postReviewReq.setUserIdx(userIdx);
            postReviewReq.setProductIdx(productIdx);
            reviewService.patchReview(postReviewReq);
            String result = "수정되었습니다.";
            return new BaseResponse(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }




}
