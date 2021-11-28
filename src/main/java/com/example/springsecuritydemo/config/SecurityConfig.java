package com.example.springsecuritydemo.config;

import com.example.springsecuritydemo.filter.RestAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.print.attribute.standard.Media;
import java.util.Map;

/**
 * @author Cao Study
 * @description <h1>SecurityConfig</h1>
 * @date 2021-11-27 16:20
 */
//debug设置为true(默认false)会有一些额外的调试日志(比如请求头)
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
@Import(SecurityProblemSupport.class)
@Order(99)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ObjectMapper objectMapper;
    private final SecurityProblemSupport securityProblemSupport;

    private RestAuthenticationFilter restAuthenticationFilter() throws Exception {
        RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandle());
        filter.setAuthenticationFailureHandler(getAuthenticationFailureHandler());
        //authenticationManager()是父类中来的
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/authorize/login");
        return filter;
    }

    private final static Logger log = LoggerFactory.getLogger(SecurityConfig.class);


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // 配置只有下面这些请求走的是rest api的登录
                .requestMatchers(req -> req.mvcMatchers("/api/**", "/admin/**", "/authorize/**"))
                // 配置Session
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置安全问题都由securityProblemSupport处理(非安全问题还是有@ControllerAdvice处理)
                .exceptionHandling(exp -> exp
                        .authenticationEntryPoint(securityProblemSupport)
                        .accessDeniedHandler(securityProblemSupport))
                .authorizeRequests(req -> req
                        .antMatchers("/authorize/**").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/api/**").hasRole("USER")
                        .anyRequest().authenticated())
                //At表示用现有的替换原来的
                .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.ignoringAntMatchers("/authorize/**", "/admin/**", "/api/**"))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        //BCryptPasswordEncoder，还有Pbkdf2PasswordEncoder
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //这个方法不进入过滤器链（一般用于忽略静态资源），而上面那个方法是进入过滤器链进行判断的
        //rest api没有静态资源，所以不用配置静态资源
        web.ignoring().mvcMatchers("/error/**");
    }

    private AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            val objectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            //val是lombok提供的，相当于final Map<String,String>
            val errData = Map.of(
                    "title", "认证失败",
                    "detail", exception.getMessage()
            );
            response.getWriter().println(errData);
        };
    }

    private AuthenticationSuccessHandler jsonAuthenticationSuccessHandle() {
        return (req, res, auth) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            res.setStatus(HttpStatus.OK.value());
            //将auth变量json字符串化后返回给前端页面
            res.getWriter().println(objectMapper.writeValueAsString(auth));
            log.debug("认证成功");
        };
    }

    /**
     * 退出登录成功处理
     *
     * @return
     */
    private LogoutSuccessHandler jsonLogoutSuccessHandler() {
        return (req, res, auth) -> {
            if (auth != null && auth.getDetails() != null) {
                req.getSession().invalidate();
            }
            res.setStatus(HttpStatus.OK.value());
            res.getWriter().println();
            log.debug("成功退出登录");
        };
    }
}
