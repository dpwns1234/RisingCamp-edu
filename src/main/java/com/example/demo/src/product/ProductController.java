package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;


    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    /**
     * 하나의 상품 조회 API
     * [GET] /product/:productIdx
     * 제목, 내용, 이미지(여러 개), 가격, 배송 타입, 판매량, 남은 개수, 도착 정보, 별점, 리뷰 개수, 판매자 이름 조회
     * @return BaseResponse<GetProductRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{productIdx}") // (GET) 127.0.0.1:9000/app/product
    public BaseResponse<GetProductRes> getProduct(@PathVariable("productIdx") int productIdx) {
        try{
            GetProductRes getProductRes = productProvider.getProduct(productIdx);
            return new BaseResponse<>(getProductRes);
        }
        catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 이름으로 상품들 조회 API
     * [GET] /app/product/?productName=
     * 제목, 이미지(하나), 가격, 배송 타입, 도착 정보, 별점, 리뷰 수 (세일 전 가격, 쿠팡 추천 이것들은 나중에 디테일로) 조회
     * @return BaseResponse<List<GetProductNameRes>>
     */
    @ResponseBody
    @GetMapping("/name") // (GET) 127.0.0.1:9000/app/product/name
    public BaseResponse<List<GetProductNameRes>> getProductName(@RequestParam(value = "productName", required = false) String productName) {
        // Get Product
        try{
            List<GetProductNameRes> getProductNameRes = productProvider.getProductByName(productName);
            return new BaseResponse<>(getProductNameRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 카테고리로 상품 조회 API
     * GET /app/product/category/?categoryName=
     * 제목, 이미지(하나), 가격, 배송 타입, 도착 정보, 별점, 리뷰 수 (세일 전 가격, 쿠팡 추천 이것들은 나중에 디테일로) 조회
     * return List<BaseResponse<GetProductCategoryRes>>
     */
    @ResponseBody
    @GetMapping("/category")
    public BaseResponse<List<GetProductCategoryRes>> getCategoryName(@RequestParam(value = "category", required = false) String category) {
        try {
            List<GetProductCategoryRes> getProductCategoryRes = productProvider.getProductCategory(category);
            return new BaseResponse<>(getProductCategoryRes);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    /**
     * Fresh 상품만 조회 API
     * GET /app/product/fresh
     * title, price, deliveryType, arrivalTime, category.name, starRating, reviewNum
     *
     * GET /app/product/fresh/?category=
     * return BaseResponse<List<GetProductFreshRes>
     */
    @ResponseBody
    @GetMapping("/fresh")
    public BaseResponse<List<GetProductFreshRes>> getProductFresh(@RequestParam(value="category", required = false) String category) {
        try {
            // fresh 상품 전체 조회
            if (category == null) {
                List<GetProductFreshRes> getProductFreshRes = productProvider.getProductFresh();
                return new BaseResponse<>(getProductFreshRes);
            }

            List<GetProductFreshRes> getProductFreshRes = productProvider.getFreshByCategory(category);
            return new BaseResponse<>(getProductFreshRes);
        }catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    /**
     * 상품의 리뷰 전체 조회 API
     * userName, starRating, product.title, content, helpNum, satisfaction, optionSatisfaction
     * [GET] /app/products/:productIdx/review
     * return BaseResponse<List<GetProductReviewRes>>
     */
    @ResponseBody
    @GetMapping("/{productIdx}/review")
    public BaseResponse<GetProductReviewRes> getProductReview(@PathVariable("productIdx") int productIdx) {
        try {
            GetProductReviewRes getProductReviewRes = productProvider.getProductReview(productIdx);
            //GetReviewAverage getReviewAverage = ; // 이건 하나밖에 필요 없는데 따로 메소드 만들어야 되나?

            return new BaseResponse<>(getProductReviewRes);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    /**
     * 상품 문의 작성 API
     * [POST] /app/products/:userIdx/:productIdx/inquiry
     * @return String
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{productIdx}/inquiry")
    public BaseResponse<String> getInquiry(@PathVariable("userIdx") int userIdx, 
                                           @PathVariable("productIdx") int productIdx,
                                           @RequestBody PostInquiryReq postInquiryReq) {
        try {
            // 아무 내용도 없으면 에러 메세지
            if (postInquiryReq.getContent() == null) {
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_INQUIRY);
            }

            // jwt으로 userIdx 추출
            int userIdxByJwt = jwtService.getUserIdx();
            // 권한없는 유저
            if (userIdxByJwt != userIdx)
                return new BaseResponse<>(INVALID_USER_JWT);

            postInquiryReq.setUserIdx(userIdx);
            postInquiryReq.setProductIdx(productIdx);
            String result = productService.postInquiry(postInquiryReq);

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 상품 문의 전체 조회 API
     * // user.email, inquiry.createdAt, content ( 나중에 seller 도 만들어주기 셀러 테이블 데이터가 없어서, 그리고 jwt도 없어서 만들려면 오래 걸릴 것 같음 )
     * [GET] /app/products/:productIdx/inquiry
     * return BaseResponse<List<GetInquiryRes>>
     */
    @ResponseBody
    @GetMapping("/{productIdx}/inquiry")
    public BaseResponse<List<GetInquiryRes>> getProductInquiry(@PathVariable("productIdx") int productIdx) {
        try {
            List<GetInquiryRes> getInquiryRes = productProvider.getProductInquiry(productIdx);
            return new BaseResponse<>(getInquiryRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    /**
     * 상품 문의 삭제 API
     * [PATCH] /app/products/:userIdx/:inquiryIdx/inquiry/status
     * return String
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/inquiry/{inquiryIdx}/status")
    public BaseResponse<String> modifyReview(@PathVariable("userIdx") int userIdx,
                                             @PathVariable("inquiryIdx") int inquiryIdx) {
        try {
            // jwt으로 userIdx 추출
            int userIdxByJwt = jwtService.getUserIdx();
            // 권한없는 유저
            if (userIdxByJwt != userIdx)
                return new BaseResponse<>(INVALID_USER_JWT);

            productService.deleteInquiry(userIdx, inquiryIdx);
            String result = "삭제 되었습니다.";
            return new BaseResponse(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
