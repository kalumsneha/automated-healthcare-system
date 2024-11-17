package com.titans.ahs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.titans.ahs.exception.type.DuplicateException;
import com.titans.ahs.exception.type.NotFoundException;
import com.titans.ahs.exception.type.ServiceException;
import com.titans.ahs.exception.type.UnauthorizedException;
import com.titans.ahs.model.User;
import com.titans.ahs.model.enums.Role;
import com.titans.ahs.repository.UserRepository;
import com.titans.ahs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) throws JsonProcessingException {

        log.info("Creating User: {}", new ObjectMapper().writeValueAsString(user));
        try{
            //encrypt password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            //set default role to patient if not provided
            if(user.getRole() == null)
                user.setRole(Role.PATIENT);
            return this.userRepository.save(user);
        }
        catch (ConstraintViolationException | DataIntegrityViolationException ex){
            log.error("Unable to create user. {}", ex.getMessage());
            throw new DuplicateException("Duplicate Entry");
        }
        catch (Exception ex){
            log.error("Unable to create user. {}", ex.getMessage());
            throw new ServiceException("Unable to create user");
        }
    }

    @Override
    public List<User> getUsers(){
        log.info("Fetching Users");
        return this.userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(String username, User user) {
        User userToUpdate = this.userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Could not find user by the provided username"));
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole() != null)
            userToUpdate.setRole(user.getRole());
        return this.userRepository.save(userToUpdate);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByUsername(username);

        Set<GrantedAuthority> authorities = null;
        if(user.isPresent()){
            authorities = Set.of(new SimpleGrantedAuthority("ROLE_".concat(user.get().getRole().name())));

            return new org.springframework.security.core.userdetails.User(
                    username,
                    user.get().getPassword(),
                    authorities
            );
        }

        throw new UnauthorizedException("User not found. Unauthorized!");


    }

}
