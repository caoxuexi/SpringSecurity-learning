package com.example.springsecuritydemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Map;

/**
 * @author Cao Study
 * @description <h1>LoginSecurityConfig</h1>
 * @date 2021-11-28 19:00
 */
@Configuration
@Order(100)
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(LoginSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置表单登录和登录页路由，permitAll表示该路由不需要验证
        http
                .formLogin(form -> form.loginPage("/login")
                        .usernameParameter("username")
                        //登录成功时，转到的路由
                        .defaultSuccessUrl("/")
                        //登录失败的url
                        .failureUrl("/login?error")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/login")
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("someSecret")
                        .tokenValiditySeconds(24 * 60 * 60))
                .authorizeRequests(req -> req.anyRequest().authenticated());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        //这个方法不进入过滤器链（一般用于忽略静态资源），而上面那个方法是进入过滤器链进行判断的
        //过滤掉/public/**,不进入过滤器链
        web.ignoring().mvcMatchers("/error/**")
                // 把页面里常见的静态资源路径都加入ignoring里面
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
