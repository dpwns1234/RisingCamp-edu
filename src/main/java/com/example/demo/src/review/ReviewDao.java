package com.example.demo.src.review;

import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PostReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkBoughtProduct(int userIdx, int productIdx) {
        String getQuery = "select exists(select * from boughtproduct where userIdx=? and  productIdx=?)";
        Object[] getParams = new Object[]{userIdx, productIdx};

        return this.jdbcTemplate.queryForObject(getQuery, int.class, getParams);
    }

    public int checkReviewExist(int userIdx, int productIdx) {
        String getQuery = "select exists(select * from review where userIdx=? and  productIdx=?)";
        Object[] getParams = new Object[]{userIdx, productIdx};

        return this.jdbcTemplate.queryForObject(getQuery, int.class, getParams);
    }
    // 별점만 등록
    public void postReview(PostReviewReq postReviewReq){
        String getReviewQuery = "insert into review (productIdx, userIdx, starRating) values(?, ?, ?)";
        Object[] getReveiwParams = new Object[]{postReviewReq.getProductIdx(), postReviewReq.getUserIdx(),
                postReviewReq.getStarRating()};
        this.jdbcTemplate.update(getReviewQuery, getReveiwParams);

    }
    // content 수정
    public void updateStarRating(PostReviewReq postReviewReq){
        int starRating = postReviewReq.getStarRating();

        String getReviewQuery = "update review set starRating = ? where productIdx = ? and userIdx = ?";
        Object[] getReveiwParams = new Object[]{starRating, postReviewReq.getProductIdx(), postReviewReq.getUserIdx()};
        this.jdbcTemplate.update(getReviewQuery, getReveiwParams);
    }
    // content 수정
    public void updateContent(PostReviewReq postReviewReq){
        String content = postReviewReq.getContent();

        String getReviewQuery = "update review set content = ? where productIdx = ? and userIdx = ?";
        Object[] getReveiwParams = new Object[]{content, postReviewReq.getProductIdx(), postReviewReq.getUserIdx()};
        this.jdbcTemplate.update(getReviewQuery, getReveiwParams);
    }
    // satisfaction 수정
    public void updateSatisfaction(PostReviewReq postReviewReq){
        String satisfaction = postReviewReq.getSatisfaction();

        String getReviewQuery = "update review set satisfaction = ? where productIdx = ? and userIdx = ?";
        Object[] getReveiwParams = new Object[]{satisfaction, postReviewReq.getProductIdx(), postReviewReq.getUserIdx()};
        this.jdbcTemplate.update(getReviewQuery, getReveiwParams);
    }
    // optionSatisfaction 수정
    public void updateOptionSatisfaction(PostReviewReq postReviewReq){
        String optionSatisfaction = postReviewReq.getOptionSatisfaction();

        String getReviewQuery = "update review set optionSatisfaction = ? where productIdx = ? and userIdx = ?";
        Object[] getReveiwParams = new Object[]{optionSatisfaction, postReviewReq.getProductIdx(), postReviewReq.getUserIdx()};
        this.jdbcTemplate.update(getReviewQuery, getReveiwParams);
    }

    public List<GetReviewRes> getReview(int userIdx) {
        String getReviewQuery =
                "select title, image, starRating, review.createdAt, review.content, satisfaction, optionSatisfaction from review\n" +
                        "left join product on review.productIdx = product.productIdx\n" +
                        "left join productimg on product.productIdx = productimg.productIdx\n" +
                        "where userIdx = ?";
        int getParam = userIdx;
        return this.jdbcTemplate.query(getReviewQuery,
                (rs,rowNum) -> new GetReviewRes(
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getInt("starRating"),
                        rs.getString("createdAt"),
                        rs.getString("content"),
                        rs.getString("satisfaction"),
                        rs.getString("optionSatisfaction")),
                getParam
        );
    }
}



