package com.example.springsecuritydemo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Cao Study
 * @description <h1>RestAuthenticationFilter</h1>
 * @date 2021-11-27 21:13
 */
//有参构造方法自动生成(springBoot机制会通过构造函数去注入bean)
@RequiredArgsConstructor
public class RestAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;

    /**
     * {
     *     "username":"张三",
     *     "password":"123456"
     * }
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken;
        try {
            InputStream is=request.getInputStream();
            val jsonNode = objectMapper.readTree(is);
            String username=jsonNode.get("username").textValue();
            String password=jsonNode.get("password").textValue();
            authenticationToken=new UsernamePasswordAuthenticationToken(
                    username,password
            );

        } catch (IOException e) {
            e.printStackTrace();
            throw new BadCredentialsException("没有找到用户名或密码");
        }
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
