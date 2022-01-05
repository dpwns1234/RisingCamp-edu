package com.example.demo.src.product;


import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetProductRes getProduct(int productIdx){
        String getProductQuery =
                "select title, product.content as content, price, deliveryType, saleVolume, remainNum, arrivalTime\n" +
                        ",round(avg(starRating), 2) as StarRating, count(review.reviewIdx) as reviewNum\n" +
                        ",s.name as sellerName\n" +
                        "from product\n" +
                        "left join review on product.productIdx = review.productIdx\n" +
                        "left join productimg p on product.productIdx = p.productIdx\n" +
                        "left join seller s on s.sellerIdx = product.sellerIdx\n" +
                        "where product.productIdx= ?";
        int getProductParams = productIdx;
        return this.jdbcTemplate.queryForObject(getProductQuery,
                (rs,rowNum) -> new GetProductRes(
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getInt("saleVolume"),
                        rs.getInt("remainNum"),
                        rs.getString("arrivalTime"),
                        rs.getInt("starRating"),  // getDouble 인가?
                        rs.getInt("reviewNum"),
                        rs.getString("sellerName")),
                getProductParams);
    }

    public List<GetProductNameRes> getProductByName(String productName) {
        String getProductQuery =
                "select title, productimg.image, price, deliveryType, arrivalTime\n" +
                        ",round(avg(starRating), 2) as StarRating, count(reviewIdx) as reviewNum\n" +
                        "from product\n" +
                        "left join review on product.productIdx = review.productIdx\n" +
                        "left join productimg on product.productIdx = productimg.productIdx\n" +
                        "where product.title like CONCAT('%', ?, '%') group by title";
        String getProductParams = productName;
        return this.jdbcTemplate.query(getProductQuery,
                (rs,rowNum) -> new GetProductNameRes(
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getString("arrivalTime"),
                        rs.getInt("starRating"),
                        rs.getInt("reviewNum")
                ),
                getProductParams);
    }

    public List<GetProductCategoryRes> getProductCategory(String category){
        String getProductQuery =
                "select category.name as categoryName, title, image, price, deliveryType, arrivalTime\n" +
                        ",round(avg(starRating), 2) as StarRating, count(reviewIdx) as reviewNum\n" +
                        "from product\n" +
                        "left join review on product.productIdx = review.productIdx\n" +
                        "left join productimg on product.productIdx = productimg.productIdx\n" +
                        "left join category on product.categoryIdx = category.categoryIdx\n" +
                        "where category.name = ? group by title";

        String getProductParam = category;
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetProductCategoryRes(
                        rs.getString("categoryName"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getString("arrivalTime"),
                        rs.getInt("starRating"),
                        rs.getInt("reviewNum")
                ),
                getProductParam
                );
    }

    public List<GetProductFreshRes> getProductFresh(){
        String productQuery =
                "select title, price, deliveryType, arrivalTime,\n" +
                        "       category.name as category,\n" +
                        "       round(avg(starRating), 2) as starRating,\n" +
                        "       count(reviewIdx) as reviewNum,\n" +
                        "       image\n" +
                        "from product\n" +
                        "left join review on product.productIdx = review.productIdx\n" +
                        "    left join category on product.categoryIdx = category.categoryIdx\n" +
                        "    left join productimg on product.productIdx = productimg.productIdx\n" +
                        "where deliveryType='Fresh'\n" +
                        "group by title";

        return this.jdbcTemplate.query(productQuery,
                (rs, rowNum) -> new GetProductFreshRes(
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getString("arrivalTime"),
                        rs.getString("category"),
                        rs.getInt("starRating"),
                        rs.getInt("reviewNum")
                )
        );
    }

    public List<GetProductFreshRes> getFreshByCategory(String category){
        String getProductQuery =
                "select title, price, deliveryType, arrivalTime,\n" +
                        "       category.name as category,\n" +
                        "       round(avg(starRating), 2) as starRating,\n" +
                        "       count(reviewIdx) as reviewNum,\n" +
                        "       image\n" +
                        "from product\n" +
                        "left join review on product.productIdx = review.productIdx\n" +
                        "    left join category on product.categoryIdx = category.categoryIdx\n" +
                        "    left join productimg on product.productIdx = productimg.productIdx\n" +
                        "where deliveryType='Fresh' and category.name= ? \n" +
                        "group by title";
        String getProductParam = category;
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetProductFreshRes(
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getString("arrivalTime"),
                        rs.getString("category"),
                        rs.getInt("starRating"),
                        rs.getInt("reviewNum")
                ),
                getProductParam
        );
    }

    public GetProductReviewRes getProductReview(int productIdx) {
        String getReviewResQuery =
                "select count(reviewIdx) as reviewNum, round(avg(starRating), 2) as starRating,\n" +
                        "round(count(if(satisfaction='good', 1, null))/count(if(satisfaction!='null', 1, null))*100, 0) as satisfactionGoodPercent,\n" +
                        "round(count(if(satisfaction='normal', 1, null))/count(if(satisfaction!='null', 1, null))*100, 0) as satisfactionNormalPercent,\n" +
                        "round(count(if(satisfaction='bad', 1, null))/count(if(satisfaction!='null', 1, null))*100, 0) as satisfactionBadPercent,\n" +
                        "round(count(if(optionSatisfaction='good', 1, null))/count(if(optionSatisfaction!='null', 1, null))*100, 0) as optionGoodPercent,\n" +
                        "round(count(if(optionSatisfaction='normal', 1, null))/count(if(optionSatisfaction!='null', 1, null))*100, 0) as optionNormalPercent,\n" +
                        "round(count(if(optionSatisfaction='bad', 1, null))/count(if(optionSatisfaction!='null', 1, null))*100, 0) as optionBadPercent\n" +
                        "from review where review.productIdx = ?;";
        String getImagesAllQuery =
                "select image from reviewimg\n" +
                "left join review on reviewimg.reviewIdx = review.reviewIdx where productIdx = ? order by helpfulNum DESC;";

        String getReviewQuery =
                "select user.name as userName, starRating, product.title as productTitle, review.content, " +
                        "helpfulNum, satisfaction, optionSatisfaction, review.reviewIdx, review.createdAt from review\n" +
                        "left join user on review.userIdx = user.userIdx\n" +
                        "left join product on review.productIdx = product.productIdx\n" +
                        "left join reviewimg on review.reviewIdx = reviewimg.reviewIdx\n" +
                        "where review.productIdx = ? group by reviewIdx";
        int getReviewParam = productIdx;
        String getImagesQuery = "select image from reviewimg where reviewIdx = ?";

        return this.jdbcTemplate.queryForObject(getReviewResQuery,
                (rs, rowNum) -> new GetProductReviewRes(
                        rs.getInt("starRating"),
                        rs.getInt("reviewNum"),
                        rs.getInt("satisfactionGoodPercent"),
                        rs.getInt("satisfactionNormalPercent"),
                        rs.getInt("satisfactionBadPercent"),
                        rs.getInt("optionGoodPercent"),
                        rs.getInt("optionNormalPercent"),
                        rs.getInt("optionBadPercent"),
                        this.jdbcTemplate.query(getImagesAllQuery,
                                (rs1, rowNum1) -> new String(
                                        rs1.getString("image")
                        ), getReviewParam),
                        this.jdbcTemplate.query(getReviewQuery,
                            (rs2, rowNum2) -> new GetProductReview(
                                    rs2.getString("userName"),
                                    rs2.getInt("starRating"),
                                    rs2.getString("productTitle"),
                                    rs2.getString("content"),
                                    rs2.getInt("helpfulNum"),
                                    rs2.getString("satisfaction"),
                                    rs2.getString("optionSatisfaction"),
                                    this.jdbcTemplate.query(getImagesQuery,
                                            (rs3, rowNum3) -> new String(
                                                    rs3.getString("image")
                                            ),
                                            rs2.getInt("reviewIdx")
                                    )
                            ), getReviewParam
                    )
                ), getReviewParam
        );
    }

    public String postInquiry(PostInquiryReq postInquiryReq) {
        String getProductQuery = "insert into inquiry (userIdx, productIdx, content) values(?, ?, ?)";
        Object [] getproductParams = new Object[]{postInquiryReq.getUserIdx(), postInquiryReq.getProductIdx(), postInquiryReq.getContent()};
        this.jdbcTemplate.update(getProductQuery, getproductParams);
        return postInquiryReq.getContent();
    }

    public List<GetInquiryRes> getProductInquiry(int productIdx){
        String getProductQuery = "select user.email as userEmail, inquiry.createdAt as inquiryCreatedAt, inquiry.content from inquiry\n" +
                "left join user on inquiry.userIdx = user.userIdx where productIdx = ?";
        int getProductParam = productIdx;
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetInquiryRes(
                        rs.getString("userEmail"),
                        rs.getString("inquiryCreatedAt"),
                        rs.getString("content")
                        ),
                getProductParam
        );
    }

    public int checkInquiryExist(int inquiryIdx) {
        // 작성하지않았거나 삭제된 상태면 0 리턴 그렇지 않으면 1 리턴
        String getQuery = "select exists(select * from inquiry where inquiryIdx=? and status != 'None')";
        int getParam = inquiryIdx;

        return this.jdbcTemplate.queryForObject(getQuery, int.class, getParam);
    }

    public void deleteInquiry(int inquiryIdx) {
        String getQuery = "update inquiry set status = 'None' where inquiryIdx = ?";
        int getParam = inquiryIdx;
        this.jdbcTemplate.update(getQuery, getParam);
    }
}
