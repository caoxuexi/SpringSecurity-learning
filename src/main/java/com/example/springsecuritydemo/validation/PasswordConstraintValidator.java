package com.example.springsecuritydemo.validation;

import com.example.springsecuritydemo.validation.annotation.ValidPassword;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.passay.*;
import org.passay.spring.SpringMessageResolver;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @author Cao Study
 * @description <h1>PasswordValidator</h1>
 * @date 2021-11-28 11:44
 */
@RequiredArgsConstructor
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword,String> {
    private final SpringMessageResolver springMessageResolver;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //这个PasswordValidator，以及下面的PasswordData都是passay框架的类
        //为了国际化，我们第一个参数传入SpringMessageResolver，不传也是可以的
        val validator=new PasswordValidator(springMessageResolver,Arrays.asList(
                //长度为8-30之间
                new LengthRule(8,30),
                //至少一个大写字母
                new CharacterRule(EnglishCharacterData.UpperCase,1),
                //至少一个小写字母
                new CharacterRule(EnglishCharacterData.LowerCase,1),
                //至少一个特殊字符
                new CharacterRule(EnglishCharacterData.Special,1),
                //不允许五个连续是字母
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical,5,false),
                //不允许五个连续是数字
                new IllegalSequenceRule(EnglishSequenceData.Numerical,5,false),
                //不允许键盘上连续的5个键比如qwert
                new IllegalSequenceRule(EnglishSequenceData.USQwerty,5,false),
                new WhitespaceRule()
        ));
        val result =validator.validate(new PasswordData(value));
        //禁用默认的分隔符，使用，去分隔各个验证消息
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(String.join(",",validator.getMessages(result)))
                .addConstraintViolation();
        return result.isValid();
    }

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
