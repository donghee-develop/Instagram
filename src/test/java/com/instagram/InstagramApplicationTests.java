package com.instagram;

import com.instagram.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InstagramApplicationTests {

    @Autowired
    EmailService emailService;
    @Test
    void contextLoads() throws  Exception{
        emailService.send_signup_auth_mail("skks1000@naver.com");

    }
}
