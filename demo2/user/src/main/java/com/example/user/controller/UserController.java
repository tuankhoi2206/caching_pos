package com.example.user.controller;

import com.example.user.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    @GetMapping("userInfo/{id}")
    public User getUserInfo(@PathVariable String id) {
        User user1 = new User();
        user1.setId("1234");
        user1.setName("John");
        user1.setPosition("developer");

        User user2 = new User();
        user2.setId("4567");
        user2.setName("Mary");
        user2.setPosition("SM");

        log.info("Getting information of {}.",  id.equals("1234") ? "John" : "Mary");
        return id.equals("1234") ? user1: user2;
    }
}
