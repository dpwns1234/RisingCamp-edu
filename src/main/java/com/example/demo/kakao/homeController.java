package com.example.demo.kakao;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // RestController 로 바꿔야하나?
@RequestMapping("/kakao")
public class homeController {
    private KakaoAPI kakaoApi = new KakaoAPI();

    @GetMapping("/logIn")
    public void logIn(@RequestParam("code") String authorizationCode) {
        //ModelAndView mav = new ModelAndView();
        // 1번 인증코드 요청 전달
        //String accessToken = kakaoApi.getAccessToken(code);
        //System.out.println("<home Controller> access Token = " + accessToken);
        // 2번 인증코드로 토큰 전달 (일단 이건 보류)
        // HashMap<String, Object> userInfo = kakaoApi

        System.out.println("<home Controller>start ");
        kakaoApi.getAccessToken2(authorizationCode);
        // System.out.println("<home Controller> access Token = " + accessToken);

    }
}
