package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
        try{
            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
                    }


    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email); // 있으면 1 없으면 0
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkId(String id) throws BaseException {
        try{
            return userDao.checkId(id);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPhoneNum(String phoneNum) throws BaseException {
        try{
        return userDao.checkPhoneNum(phoneNum);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);

        // 탈퇴한 계정 Validation
        if(user.getStatus().equals("Inactive")) // 이거 안되면 반대로 ㄱㄱ
            throw new BaseException(INACTIVE_USER);


        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userIdx = user.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public List<GetBasketRes> getBasket(int userIdx) throws BaseException {
        try {
            List<GetBasketRes> getBasket = userDao.getBasket(userIdx);
            return getBasket;
        } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPickRes> getPick(int userIdx) throws BaseException {
        try {
            List<GetPickRes> getPick = userDao.getPick(userIdx);
            return getPick;
        } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetBoughtProductRes> getBoughtProduct (int userIdx) throws BaseException {
        try {
            List<GetBoughtProductRes> getBoughtProduct = userDao.getBoughtProduct(userIdx);
            return getBoughtProduct;
        } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSawProductRes> getSawProduct (int userIdx) throws BaseException {
        try {
        List<GetSawProductRes> getSawProduct = userDao.getSawProduct(userIdx);
        return getSawProduct;
         } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
