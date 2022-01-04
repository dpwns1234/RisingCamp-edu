package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ReviewProvider {
    private final ReviewDao reviewDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public int checkBoughtProduct(int userIdx, int productIdx) throws BaseException {
    //    try {
            return reviewDao.checkBoughtProduct(userIdx, productIdx);
    //    } catch (Exception exception) {
    //        throw new BaseException(DATABASE_ERROR);
    //    }
    }

    public int checkReviewExist(int userIdx, int productIdx) throws BaseException {
        //   try {
        return reviewDao.checkReviewExist(userIdx, productIdx);
        //    } catch (Exception exception) {
        //        throw new BaseException(DATABASE_ERROR);
        //    }
    }

    public List<GetReviewRes> getReview(int userIdx) throws BaseException {
        try {
            List<GetReviewRes> getReviewRes = reviewDao.getReview(userIdx);
            return getReviewRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

