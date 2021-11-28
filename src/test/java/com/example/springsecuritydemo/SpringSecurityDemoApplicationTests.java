package com.example.springsecuritydemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringSecurityDemoApplicationTests {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void contextLoads() {
    }

    @Test
    void testEncoder() {
        String newpassword = encoder.encode("123456");
        System.out.println(newpassword);
    }
}
