package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        try{
            if(Email == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(Email);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // 이메일이 Null 값이면 Validation
        if(postUserReq.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        // 주소가 null 값이면 validation
        if(postUserReq.getAddress() == null) { // || postUserReq.getAddress() == "" 이것도 해줘야 하지 않나?
            return new BaseResponse<>(POST_USERS_EMPTY_ADDRESS);
        }
        // 이름이 null 값이면 validation
        if(postUserReq.getName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        // 아이디가 null 값이면 validation
        if(postUserReq.getId() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_ID);
        }
        // 비밀번호가 null 값이면 validation
        if(postUserReq.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.

            // 아이디가 null 값이면 validation
            if(postLoginReq.getId() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_ID);
            }
            // 비밀번호가 null 값이면 validation
            if(postLoginReq.getPassword() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }


            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/modify")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserReq patchUserReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 변경 시작
            patchUserReq.setUserIdx(userIdx); // pathVariable 로 받은 값을 넣어준다.
            userService.modifyUser(patchUserReq);
            String result = "성공했어용";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 장바구니 조회 API
     * GET /app/users/{userIdx}/basket
     * return BaseResponse<GetBasketRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/basket")
    public BaseResponse<List<GetBasketRes>> getBasketRes(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try {
            List<GetBasketRes> getBasketRes = userProvider.getBasket(userIdx);
            return new BaseResponse<>(getBasketRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 장바구니 담기 API
     * [POST] /app/users/{userIdx}/basket
     * return String
     */
    @ResponseBody
    @PostMapping("/{userIdx}/basket")
    public BaseResponse<String> postBasketRes(@PathVariable("userIdx") int userIdx,
                                                         @RequestBody PostBasketReq postBasketReq) {
        // Get Users
        try {
            userService.postBasket(userIdx, postBasketReq);
            return new BaseResponse<>("성공했어요");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 찜한 상품 API
     * GET /app/users/{userIdx}/pick
     * return BaseResponse<List<GetUserPickRes>>
     *     title, price, image, deliveryType, saleVolume( 품절되면 그에 맞는 값만 내보내기, status 확인, 세일 적용 )
     *
     */
    @ResponseBody
    @GetMapping("/{userIdx}/pick")
    public BaseResponse<List<GetPickRes>> getPickRes(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try {
            List<GetPickRes> getPickRes = userProvider.getPick(userIdx);
            return new BaseResponse<>(getPickRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 주문목록 API
     * GET /app/users/{userIdx}/bought-product
     * @return BaseResponse<List<GetBoughtProductRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/bought-product")
    public BaseResponse<List<GetBoughtProductRes>> getBoughtProductRes(@PathVariable("userIdx") int userIdx) {
        try {
            List<GetBoughtProductRes> getBoughtProductRes = userProvider.getBoughtProduct(userIdx);
            return new BaseResponse<>(getBoughtProductRes);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    /**
     * 최근 본 상품 조회 API
     * GET /app/users/{userIdx}/saw-product
     * @return BaseResponse<List<GetBoughtProductRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/saw-product")
    public BaseResponse<List<GetSawProductRes>> getSawProductRes(@PathVariable("userIdx") int userIdx) {
        try {
            List<GetSawProductRes> getSawProductRes = userProvider.getSawProduct(userIdx);
            return new BaseResponse<>(getSawProductRes);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
