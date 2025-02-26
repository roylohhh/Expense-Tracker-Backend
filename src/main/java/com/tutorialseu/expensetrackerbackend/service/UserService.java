package com.tutorialseu.expensetrackerbackend.service;

import com.tutorialseu.expensetrackerbackend.model.AppUser;

import java.util.Optional;

public interface UserService {
    AppUser saveUser(AppUser appUser);
    AppUser findByUsername(String username);
    Optional<AppUser> findUserById(Long id);
}
