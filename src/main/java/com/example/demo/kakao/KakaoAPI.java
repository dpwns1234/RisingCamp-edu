package com.example.demo.kakao;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class KakaoAPI {
    public String getAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // http 통신을 가능케 해주는 클래스
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=12de50d05697009bf7169474e681b181");
            sb.append("&redirect_uri=https://3.37.229.159/kakao/logIn"); // 이건 생각좀 해봐야할 듯
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("response code = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body = " + result);

            //JsonParser parser = new JsonParser();
            //JsonElement element = parser.parse(result);
            //accessToken = element.getAsJsonObject().get("access_token").getAsString();
            //refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            JSONObject jsonObject = new JSONObject(result);
            accessToken = jsonObject.getString("access_token");
            refreshToken = jsonObject.getString("refresh_token");
            System.out.println("accessToken = " + accessToken);
            br.close();
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public void getAccessToken2(String authorizationCode) {
        try {
            MyOkHttpClient.getAccessToken(authorizationCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
