package com.example.springsecuritydemo.controller;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Cao Study
 * @description <h1>UserResourceController</h1>
 * @date 2021-11-27 10:20
 */
@RestController
@RequestMapping("/api")
public class UserController {
    @GetMapping("/greeting")
    public String greeting(){
        return "hello world";
    }

    @PostMapping("/greeting")
    public String makeGreeting(@RequestParam String name,@RequestBody Profile profile){
        return "hello world, "+name+"!\n"+profile;
    }

    @PostMapping("/greeting/{name}")
    public String makeGreeting(@PathVariable String name){
        return "hello world, "+name+"!\n";
    }

    @GetMapping("/principal")
    public Principal getPrincipal(Principal principal){
        //上面方法的参数都是自动传入的，但是我们也可以 用方法来获取
        Principal principal1= (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal;
    }

    @GetMapping("/authentication")
    public Authentication getAuthentication(Authentication authentication){
        //上面的参数都是自动传入的，但是我们也可以 用方法来获取
        Authentication authentication1= SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    @Data
    static class Profile{
        private String gender;
        private String idNo;
    }
}
