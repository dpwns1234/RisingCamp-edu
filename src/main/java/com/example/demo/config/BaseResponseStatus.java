package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2020, "이메일을 입력해주세요."),
    POST_USERS_EMPTY_ADDRESS(false, 2021, "주소를 입력해주세요."),
    POST_USERS_EMPTY_ID(false, 2022, "아이디를 입력해주세요."),
    POST_USERS_EMPTY_NAME(false, 2023, "이름을 입력해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2024, "비밀번호를 입력해주세요."),

    POST_USERS_INVALID_EMAIL(false, 2030, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2031,"중복된 이메일입니다."),
    POST_USERS_EXISTS_ID(false, 2032, "중복된 아이디입니다."),
    POST_USERS_EXISTS_PHONE_NUM(false, 2033, "중복된 번호입니다."),

    // [POST] /review
    POST_REVIEW_EMPTY_STARRATING(false, 2100, "별점을 입력해주세요."),

    // [POST] /products
    POST_PRODUCTS_EMPTY_INQUIRY(false, 2101, "문의내용을 입력해주세요."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    INACTIVE_USER(false, 3015, "탈퇴한 계정입니다."),

    // [POST] /review
    NOT_BOUGHT_PRODUCT(false, 3100, "해당상품을 구입하지 않았습니다."),
    ALREADY_REVIEW_EXIST(false, 3101, "이미 리뷰가 존재합니다."),

    // [POST] /products
    NOT_WRITE_INQUIRY(false, 3200, "상품 문의를 작성하지 않았습니다."),
    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4020,"유저 이름 수정 실패"),
    MODIFY_FAIL_USEREMAIL(false,4021,"유저 이메일 수정 실패"),
    MODIFY_FAIL_USERPASSWORD(false,4022,"유저 비밀번호 수정 실패"),
    MODIFY_FAIL_USERADDRESS(false,4023,"유저 주소 수정 실패"),
    MODIFY_FAIL_USERPHONENUM(false,4024,"유저 핸드폰 번호 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
