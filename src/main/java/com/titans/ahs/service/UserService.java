package com.titans.ahs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.titans.ahs.model.User;
import com.titans.ahs.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws JsonProcessingException {
        log.info("Creating User: {}", new ObjectMapper().writeValueAsString(user));
        return this.userRepository.save(user);
    }

    public List<User> getUsers(){
        log.info("Fetching Users");
        return this.userRepository.findAll();
    }
}
