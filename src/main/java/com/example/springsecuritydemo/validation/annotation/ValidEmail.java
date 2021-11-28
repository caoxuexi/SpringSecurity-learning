package com.example.springsecuritydemo.validation.annotation;

import com.example.springsecuritydemo.validation.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Cao Study
 * @description <h1>ValidEmail</h1>
 * @date 2021-11-28 11:20
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.ANNOTATION_TYPE})  // 约束注解应用的目标元素类型
@Retention(RetentionPolicy.RUNTIME)  // 约束注解应用的时机
@Constraint(validatedBy = EmailValidator.class) // 与约束注解关联的验证器
@Documented
public @interface ValidEmail {
    String message() default "{ValidEmail.email}";  // 约束注解验证时的输出消息
    //下面两个在校验中并无太大作用，但是不写不行
    Class<?>[] groups() default {};  // 约束注解验证时的输出消息
    Class<? extends Payload>[] payload() default {};  // 约束注解的有效负载

}
