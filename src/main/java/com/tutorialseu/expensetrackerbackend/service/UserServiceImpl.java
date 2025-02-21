package com.tutorialseu.expensetrackerbackend.service;

import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public AppUser saveUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    @Override
    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Optional<AppUser> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
