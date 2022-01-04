package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PostInquiryReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService;


    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService = jwtService;

    }

    public String postInquiry(PostInquiryReq postInquiryReq) throws BaseException {
        try {

            int userIdx = postInquiryReq.getUserIdx();
            int productIdx = postInquiryReq.getProductIdx();

            // 별점만 필수로 입력하면 되고(insert into)
            String result = productDao.postInquiry(postInquiryReq);

            return result;
        } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteInquiry(int userIdx ,int inquiryIdx) throws BaseException {
     //   try {
            // 이 유저가 이미 상품 문의를 작성했는지 확인 ( 작성했으면 1, 작성하지 않았다면 0 )
            if(productProvider.checkInquiryExist(inquiryIdx) == 0) {
                throw new BaseException(NOT_WRITE_INQUIRY);
            }

            productDao.deleteInquiry(inquiryIdx);

    //    } catch(Exception e) {
   //         throw new BaseException(DATABASE_ERROR);
    //    }
    }
}
