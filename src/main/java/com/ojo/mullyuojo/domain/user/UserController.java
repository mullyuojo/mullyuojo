package com.ojo.mullyuojo.domain.user;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        return user;
    }

    @PostMapping
    public String test(){
        return "test";
    }
}
