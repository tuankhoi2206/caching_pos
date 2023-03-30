package com.example.caching.controller;

import com.example.caching.dto.User;
import com.example.caching.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        log.info("Calling User API with id: {}", id);
        return userService.getUserInfo(id);
    }

    @PutMapping("user/")
    public User updateUser(User user) {
        log.info("Calling USer API update with id: {}", user.getId());
        return new User();
    }
}
