package com.example.springsecuritydemo.validation;

import com.example.springsecuritydemo.domain.dto.UserDTO;
import com.example.springsecuritydemo.validation.annotation.PasswordMatch;
import lombok.val;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Valid;
import javax.validation.Validator;

/**
 * @author Cao Study
 * @description <h1>PasswordMatchValidator</h1>
 * @date 2021-11-28 13:51
 */
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserDTO> {
    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext context) {
        val result=userDTO.getPassword().equals(userDTO.getMatchPassword());

        return result;
    }

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
