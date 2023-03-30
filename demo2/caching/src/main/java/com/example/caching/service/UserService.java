package com.example.caching.service;

import com.example.caching.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {
    @Cacheable(value = "user", key = "#id")
    public User getUserInfo(String id) {
        RestTemplate restTemplate = new RestTemplate();
        String userUrl = "http://localhost:8081/userInfo/";
        log.info("Calling UserInfor with id: {}", id);
        return restTemplate.getForEntity(userUrl + id, User.class).getBody();
    }

}
