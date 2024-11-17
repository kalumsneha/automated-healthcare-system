package com.titans.ahs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.titans.ahs.exception.type.NotFoundException;
import com.titans.ahs.model.User;
import com.titans.ahs.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/new")
    public User createUser(@Validated @RequestBody User user) throws JsonProcessingException {

        log.info("Create User Request: {}", new ObjectMapper().writeValueAsString(user));
        return this.userService.createUser(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<User> getUsers() throws JsonProcessingException {
        return this.userService.getUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{username}")
    public Optional<User> getUserByUserId(@PathVariable String username) throws JsonProcessingException {
        log.info("fetching user by username. username: {}", username);
        return Optional.ofNullable(this.userService.getUserByUsername(username).orElseThrow(() -> new NotFoundException("Could not find user by the provided username")));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user) throws JsonProcessingException {
        log.info("updating user. username: {}, user: {}", username, new ObjectMapper().writeValueAsString(user));
        return this.userService.updateUser(username, user);
    }

}
