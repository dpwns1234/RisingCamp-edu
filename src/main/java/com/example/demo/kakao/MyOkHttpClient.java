package com.example.demo.kakao;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MyOkHttpClient {
    private static String hostURL = "https://kauth.kakao.com/oauth/token";
    private static String redirect_URi = "http://3.37.229.159/kakao/logIn";
    private static String id = "e2582b10abc0513d70250e43c7d3a575";
    private static String secret_key = "IL1HEt26yk2jGfplhF4isNV8cxEP10QR";

    public static String getAccessToken(String authorizationCode) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl urlWithParameters = makeHttpUrlWithParameters(authorizationCode);
        Request request = makeRequset(urlWithParameters);
        return client.newCall(request).execute().body().string();
        client.newCall(request).execute().body().source().
    }

    private static Request makeRequset(HttpUrl url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    private static HttpUrl makeHttpUrlWithParameters(String authorizationCode) throws MalformedURLException {
        HttpUrl.Builder httpBuilder = HttpUrl
                .get(new URL(hostURL))
                .newBuilder();

        httpBuilder.addQueryParameter("grant_type", "authorization_code");
        httpBuilder.addQueryParameter("client_id", id);
        httpBuilder.addQueryParameter("redirect_uri", redirect_URi);
        httpBuilder.addQueryParameter("code", authorizationCode);
        httpBuilder.addQueryParameter("client_secret", secret_key);
        return httpBuilder.build();
    }
}
