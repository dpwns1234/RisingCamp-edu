package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao, JwtService jwtService) {
        this.productDao = productDao;
        this.jwtService = jwtService;
    }

    public GetProductRes getProduct(int productIdx) throws BaseException {
//       try{
        GetProductRes getProductRes = productDao.getProduct(productIdx);
        return getProductRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
    }

    public List<GetProductNameRes> getProductByName(String productName) throws BaseException {
        try {
            List<GetProductNameRes> getProductNameRes = productDao.getProductByName(productName);
            return getProductNameRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductCategoryRes> getProductCategory(String category) throws BaseException {
        //    try{
        List<GetProductCategoryRes> getProductCategory = productDao.getProductCategory(category);
        return getProductCategory;
        //    } catch(Exception e) {
        //        throw new BaseException(DATABASE_ERROR);
        //    }
    }

    public List<GetProductFreshRes> getProductFresh() throws BaseException {
        try {
            List<GetProductFreshRes> getProductFresh = productDao.getProductFresh();
            return getProductFresh;
        } catch (Exception e) {
            throw new BaseException((DATABASE_ERROR));
        }
    }

    public List<GetProductFreshRes> getFreshByCategory(String category) throws BaseException {
        try {
            List<GetProductFreshRes> getProductFresh = productDao.getFreshByCategory(category);
            return getProductFresh;
        } catch (Exception e) {
            throw new BaseException((DATABASE_ERROR));
        }
    }

    public List<GetProductReview> getProductReview(int productIdx) throws BaseException {
    //    try {
            List<GetProductReview> getProductReviewRes = productDao.getProductReview(productIdx);
            return getProductReviewRes;
    //    } catch (Exception e) {
   //         throw new BaseException((DATABASE_ERROR));
    //    }
    }

    public List<GetInquiryRes> getProductInquiry(int productIdx) throws BaseException {
        try {
            List<GetInquiryRes> getInquiryRes = productDao.getProductInquiry(productIdx);
            return getInquiryRes;
        } catch (Exception e) {
            throw new BaseException((DATABASE_ERROR));
        }
    }

    public int checkInquiryExist(int inquiryIdx) throws BaseException {
           try {
        return productDao.checkInquiryExist(inquiryIdx);
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
    }
}


