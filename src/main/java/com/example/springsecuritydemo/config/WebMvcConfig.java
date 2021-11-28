package com.example.springsecuritydemo.config;

import lombok.RequiredArgsConstructor;
import org.passay.MessageResolver;
import org.passay.spring.SpringMessageResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author Cao Study
 * @description <h1>WebMvcConfig</h1>
 * @date 2021-11-27 18:37
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final MessageSource messageSource;

    @Bean
    public MessageResolver messageResolver(){
        return new SpringMessageResolver(messageSource);
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(){
        LocalValidatorFactoryBean bean=new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置静态资源路径
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/")
                .resourceChain(false);
        registry.setOrder(1);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //当访问/login路由时会自动跳转到login视图，也就是login.html
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(HIGHEST_PRECEDENCE);
    }
}
