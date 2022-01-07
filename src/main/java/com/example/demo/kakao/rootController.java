package com.example.demo.kakao;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hi")
public class rootController {
    public static String main() {
        System.out.println("hi2");
        return "index1";
    }
}
