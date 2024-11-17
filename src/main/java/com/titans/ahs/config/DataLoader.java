package com.titans.ahs.config;

import com.titans.ahs.model.User;
import com.titans.ahs.model.enums.Role;
import com.titans.ahs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Load initial data into the database
        if(userService.getUserByUsername("mfernando").isEmpty())
            userService.createUser(new User("mfernando", "password1", "Mikalum", "Fernando", "9057810000", "mfernando@gmail.com", Role.ADMIN));

        //userService.createUser(new User("mfernando", "password1", "Mikalum", "Fernando", LocalDate.of(1960, 1, 18), "9057810000", "mfernando@gmail.com", Role.ADMIN));
    }
}
