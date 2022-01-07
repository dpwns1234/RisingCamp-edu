package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from user";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("password"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from user where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from user where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("password")),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into user (name, id, password, email, phoneNum, address) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getId(),
                postUserReq.getPassword(), postUserReq.getEmail(), postUserReq.getPhoneNum(), postUserReq.getAddress()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)"; // exitsts 는 있으면 1 없으면 0 리턴
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class,
                checkEmailParams);

    }

    public int checkId(String id) {
        String checkIdQuery = "select exists(select id from user where id = ?)";
        String checkIdParams = id;
        return this.jdbcTemplate.queryForObject(checkIdQuery, int.class, checkIdParams);
    }

    public int checkPhoneNum(String phoneNum) {
        String checkPhoneQuery = "select exists(select phoneNum from user where phoneNum = ?)";
        String checkPhoneParams = phoneNum;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery, int.class, checkPhoneParams);
    }

//    public int modifyUserName(PatchUserReq patchUserReq){
//        String modifyUserNameQuery = "update user set name = ? where userIdx = ? ";
//        Object[] modifyUserNameParams = new Object[]{patchUserReq.getName(), patchUserReq.getUserIdx()};
//
//        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
//    }

    // 순서대로 이름, 이메일, 비밀번호, 주소, 핸드폰번호 수정하는 함수들
    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update user set name = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserEmail(PatchUserReq patchUserReq){
        String modifyUserQuery = "update user set email = ? where userIdx = ? ";
        Object[] modifyUserParams = new Object[]{patchUserReq.getEmail(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserQuery,modifyUserParams);
    }

    public int modifyUserPassword(PatchUserReq patchUserReq){
        String modifyUserQuery = "update user set password = ? where userIdx = ? ";
        Object[] modifyUserParams = new Object[]{patchUserReq.getPassword(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserQuery,modifyUserParams);
    }

    public int modifyUserAddress(PatchUserReq patchUserReq){
        String modifyUserQuery = "update user set address = ? where userIdx = ? ";
        Object[] modifyUserParams = new Object[]{patchUserReq.getAddress(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserQuery,modifyUserParams);
    }

    public int modifyUserPhoneNum(PatchUserReq patchUserReq){
        String modifyUserQuery = "update user set phoneNum = ? where userIdx = ? ";
        Object[] modifyUserParams = new Object[]{patchUserReq.getPhoneNum(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserQuery,modifyUserParams);
    }

    // 광고 동의
    public int modifyAgreeAdEmail(PatchUserReq patchUserReq){
        String modifyUserQuery = "update useradagree set agreeEmail = ? where userIdx = ?";
        Object[] modifyUserParams = new Object[]{patchUserReq.getAgreeAdEmail(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserQuery,modifyUserParams);
    }
    public int modifyAgreeAdPhone(PatchUserReq patchUserReq){
        String modifyUserQuery = "update useradagree set agreePhoneNum = ? where userIdx = ?";
        Object[] modifyUserParams = new Object[]{patchUserReq.getAgreeAdPhone(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserQuery,modifyUserParams);
    }
    public int modifyAgreeAdPush(PatchUserReq patchUserReq){
        String modifyUserQuery = "update useradagree set agreePush = ? where userIdx = ?";
        Object[] modifyUserParams = new Object[]{patchUserReq.getAgreeAdPush(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserQuery,modifyUserParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password,email,name, id, status from user where id = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("status")
                ),
                getPwdParams
                );

    }

    public List<GetBasketRes> getBasket(int userIdx) {
        String getProductQuery =
                "select title, image, price, deliveryType, arrivalTime\n" +
                        "from product\n" +
                        "left join productimg on product.productIdx = productimg.productIdx\n" +
                        "left join basket on product.productIdx = basket.productIdx\n" +
                        "where basket.status='Exist' and userIdx=?";
        int getUserIdxParam = userIdx;
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetBasketRes(
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getString("arrivalTime")
                ),
                getUserIdxParam
        );
    }

    public void postBasket(int userIdx, PostBasketReq postBasketReq) {
        String getQuery = "insert into basket (userIdx, productIdx, num) values (?, ?, ?)";
        Object [] getParams = new Object[] {userIdx, postBasketReq.getProductIdx(), postBasketReq.getNum()};
        this.jdbcTemplate.update(getQuery, getParams);
    }

    public List<GetPickRes> getPick(int userIdx) {
        String getProductQuery =
                "select title, image, price, round(price - price*discountRate/100,0) as discountPrice, deliveryType, remainNum\n" +
                        "from product\n" +
                        "left join productimg on product.productIdx = productimg.productIdx\n" +
                        "left join pickproduct on product.productIdx = pickproduct.productIdx\n" +
                        "where pickproduct.status='Exist' and userIdx=?;";
        int getUserIdxParam = userIdx;
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetPickRes(
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getInt("price"),
                        rs.getInt("discountPrice"),
                        rs.getString("deliveryType"),
                        rs.getInt("remainNum")
                ),
                getUserIdxParam
        );
    }

    public List<GetBoughtProductRes> getBoughtProduct(int userIdx) {
        String getProductQuery =
                "select title, image, deliveryType, price*buyNum as price, boughtproduct.status, buyNum, boughtAt,\n" +
                        "if(boughtproduct.status = 'Delivered', deliveredDay, product.arrivalTime) as deliveryDay\n" +
                        "from boughtproduct\n" +
                        "left join product on boughtproduct.productIdx = product.productIdx\n" +
                        "left join productimg on product.productIdx = productimg.productIdx\n" +
                        "where userIdx = ?";
        int getUserIdxParam = userIdx;
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetBoughtProductRes(
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getString("deliveryType"),
                        rs.getInt("price"),
                        rs.getString("status"),
                        rs.getInt("buyNum"),
                        rs.getString("boughtAt"),
                        rs.getString("deliveryDay")
                        ),
                getUserIdxParam
        );
    }

    public List<GetSawProductRes> getSawProduct(int userIdx) {
        String getProductQuery =
                "select title, price, deliveryType, image, round(avg(starRating), 2) as starRating, count(reviewIdx) as reviewNum\n" +
                        "from sawproduct\n" +
                        "left join product on sawproduct.productIdx = product.productIdx\n" +
                        "left join productimg on product.productIdx = productimg.productIdx\n" +
                        "left join review on sawproduct.productIdx = review.reviewIdx\n" +
                        "where sawproduct.userIdx = ? group by title";
        int getUserIdxParam = userIdx;
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetSawProductRes(
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getString("image"),
                        rs.getInt("starRating"),
                        rs.getInt("reviewNum")
                ),
                getUserIdxParam
        );
    }
}
