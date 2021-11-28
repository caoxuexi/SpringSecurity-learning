package com.example.springsecuritydemo.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

/**
 * @author Cao Study
 * @description <h1>ExceptionHandler</h1>
 * @date 2021-11-28 18:12
 */
@ControllerAdvice
public class ExceptionHandler implements ProblemHandling {
    @Value("${problemHandling.causalChainsEnabled}")
    private boolean causalChainsEnabled;

    @Override
    public boolean isCausalChainsEnabled() {
        //如果return ture就会将堆栈信息写到日志中去，生产环境下要关掉
        return causalChainsEnabled;
    }
}
