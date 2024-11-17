package com.titans.ahs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.titans.ahs.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User createUser(User user) throws JsonProcessingException;

    List<User> getUsers();

    Optional<User> getUserByUsername(String username);

    User updateUser(String username, User user);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
