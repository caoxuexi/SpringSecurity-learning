package com.example.springsecuritydemo.domain.dto;

import com.example.springsecuritydemo.validation.annotation.PasswordMatch;
import com.example.springsecuritydemo.validation.annotation.ValidEmail;
import com.example.springsecuritydemo.validation.annotation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Cao Study
 * @description <h1>UserDTO</h1>
 * @date 2021-11-28 10:19
 */
@Data
@PasswordMatch
public class UserDTO implements Serializable {
    @NotNull
    @NotBlank
    @Size(min = 4,max = 50,message = "用户名长度必须在4到50个字符之间")
    private String username;

    @NotNull
    @NotBlank
    @ValidPassword
    private String password;

    @NotNull
    @NotBlank
    @ValidPassword
    private String matchPassword;

    @ValidEmail
    @NotNull
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 2,max = 20,message = "姓名长度必须在2到20个字符之间")
    private String name;
}
