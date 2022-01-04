package com.example.demo.src.kakao;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/kakao")
public class KakaoController {
    private Kakao_restapi kakao_restapi=new Kakao_restapi();

    @GetMapping("/logIn")
    public String kakaoConnect() {

        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + "12de50d05697009bf7169474e681b181");
        url.append("&redirect_uri=http://3.37.229.159/kakao/logIn");
        url.append("&response_type=code");

        return "redirect:" + url.toString();
    }

    @RequestMapping(value="/callback",produces="application/json",method= {RequestMethod.GET, RequestMethod.POST})
    public String kakaoLogin(@RequestParam("code")String code, RedirectAttributes ra, HttpSession session, HttpServletResponse response ) throws IOException {


        System.out.println("kakao code:"+code);
        JsonNode access_token=kakao_restapi.getKakaoAccessToken(code);
        System.out.println(access_token);
        return "home";
    }

}
public class Kakao_restapi {
    public static JsonNode getKakaoAccessToken(String code) {
        System.out.println("restapi클래스"+code);
        final String RequestUrl = "https://kauth.kakao.com/oauth/token"; // Host
        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        String kakao_access_token = "PdT7-SW-mWLBhL7nJN5mHwyBobzSXn_aYV7xhwopcFEAAAF-AS8h2w"; // 이건 내건데 그럼 어케 받아오지 회원거는?
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakao_access_token);

        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", "각자의 키로")); // REST API KEY
        postParams.add(new BasicNameValuePair("redirect_uri", "http://3.37.229.159/kakao/callback")); // 리다이렉트 URI
        postParams.add(new BasicNameValuePair("code", code)); // 로그인 과정중 얻은 code 값

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);

        JsonNode returnNode = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(postParams));

            final HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
            System.out.println("Post parameters : " + postParams);
            System.out.println("Response Code : " + responseCode);

            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();

            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        return returnNode;
    }


}