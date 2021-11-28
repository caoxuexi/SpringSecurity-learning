package com.example.springsecuritydemo.validation;

import com.example.springsecuritydemo.validation.annotation.ValidEmail;
import lombok.val;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author Cao Study
 * @description <h1>EmailValidator</h1>
 * @date 2021-11-28 11:21
 */
public class EmailValidator implements ConstraintValidator<ValidEmail,String> {
    //邮箱的正则表达式
    private final static String EMAIL_PATTERN="^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    //判断检验合不合法，true为通过,false为失败
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateEmail(value);
    }

    private boolean validateEmail(final String email){
        val pattern= Pattern.compile(EMAIL_PATTERN);
        val matcher=pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }
}
