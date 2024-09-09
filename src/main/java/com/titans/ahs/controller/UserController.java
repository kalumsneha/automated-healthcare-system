package com.titans.ahs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.titans.ahs.model.User;
import com.titans.ahs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@Validated @RequestBody User user) throws JsonProcessingException {
        log.info("Create User Request: {}", new ObjectMapper().writeValueAsString(user));
        return this.userService.createUser(user);
    }

    @GetMapping
    public List<User> getUsers() throws JsonProcessingException {
        return this.userService.getUsers();
    }

}
