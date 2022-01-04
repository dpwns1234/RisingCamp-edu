package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 중복된 이메일이라면
        if(userProvider.checkEmail(postUserReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        // 중복된 아이디라면
        if (userProvider.checkId(postUserReq.getId()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_ID);
        }

        // 중복된 번호라면
        if (userProvider.checkPhoneNum(postUserReq.getPhoneNum()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_PHONE_NUM);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        // 회원가입했을 때 굳이 jwt 생성할 이유 x -> 나중에 없애자 ( 수정하려면 리턴 값 수정해야 함 )
        try {
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt, userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
//        try{
//            // 이름을 수정하지 않을 경우 수정하지 않고 리턴한다. (= <"name": "yj"> 이러한 body값이 없을 경우)
//            if(patchUserReq.getName() == null) // || patchUserReq.getName() == "" 만약 클라가 설정하는게 아니라면 설정해주기
//                return;
//            int result = userDao.modifyUserName(patchUserReq);
//            if(result == 0){
//                throw new BaseException(MODIFY_FAIL_USERNAME);
//            }
//        } catch(Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

    public void modifyUser(PatchUserReq patchUserReq) throws BaseException {
        try{
            // 이름 변경 / 이름을 변경하지 않았다면 넘어가기
            if(patchUserReq.getName() != null) // || patchUserReq.getName() == "" 만약 클라가 설정하는게 아니라면 설정해주기
            {
                int result = userDao.modifyUserName(patchUserReq);
                if (result == 0)
                    throw new BaseException(MODIFY_FAIL_USERNAME);
            }
            // 이메일 변경 / 이메일을 변경하지 않았다면 넘어가기
            if(patchUserReq.getEmail() != null)
            {
                int result = userDao.modifyUserEmail(patchUserReq);
                if (result == 0)
                    throw new BaseException(MODIFY_FAIL_USEREMAIL);
            }
            // 주소 변경 / 주소를 변경하지 않았다면 넘어가기
            if(patchUserReq.getAddress() != null)
            {
                int result = userDao.modifyUserAddress(patchUserReq);
                if (result == 0)
                    throw new BaseException(MODIFY_FAIL_USERADDRESS);
            }
            // 핸드폰 번호 변경 / 핸드폰 번호를 변경하지 않았다면 넘어가기
            if(patchUserReq.getPhoneNum() != null)
            {
                int result = userDao.modifyUserPhoneNum(patchUserReq);
                if (result == 0)
                    throw new BaseException(MODIFY_FAIL_USERPHONENUM);
            }
            // 비밀번호 변경 / 비밀번호를 변경하지 않았다면 넘어가기
            if(patchUserReq.getPassword() != null)
            {
                try{
                    //암호화
                    String pwd = new SHA256().encrypt(patchUserReq.getPassword());
                    patchUserReq.setPassword(pwd);
                } catch (Exception ignored) {
                    throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
                }

                int result = userDao.modifyUserPassword(patchUserReq);
                if (result == 0)
                    throw new BaseException(MODIFY_FAIL_USERPASSWORD);
            }

            // 광고 동의 변경
            // 이메일 광고 동의 변경 / 핸드폰 번호를 변경하지 않았다면 넘어가기
            if(patchUserReq.getAgreeAdEmail() != null)
            {
                int result = userDao.modifyAgreeAdEmail(patchUserReq);
                if (result == 0)
                    throw new BaseException(MODIFY_FAIL_USERNAME);
            }
            // 핸드폰 광고 동의 변경 / 핸드폰 번호를 변경하지 않았다면 넘어가기
            if(patchUserReq.getAgreeAdPhone() != null)
            {
                int result = userDao.modifyAgreeAdPhone(patchUserReq);
                if (result == 0)
                    throw new BaseException(MODIFY_FAIL_USERNAME);
            }
            // 푸시 광고 동의 변경 / 핸드폰 번호를 변경하지 않았다면 넘어가기
            if(patchUserReq.getAgreeAdPush() != null)
            {
                int result = userDao.modifyAgreeAdPush(patchUserReq);
                if (result == 0)
                    throw new BaseException(MODIFY_FAIL_USERNAME);
            }

        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void postBasket(int userIdx, PostBasketReq postBasketReq) throws BaseException {
        try {
            userDao.postBasket(userIdx, postBasketReq);
        } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
