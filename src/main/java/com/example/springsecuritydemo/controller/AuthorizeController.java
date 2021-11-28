package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.domain.dto.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Cao Study
 * @description <h1>AuthorizeController</h1>
 * @date 2021-11-28 10:23
 */
@RequestMapping("/authorize")
@RestController
public class AuthorizeController {
    @PostMapping("/register")
    public UserDTO register(@RequestBody @Valid UserDTO userDTO){
        return userDTO;
    }


}
