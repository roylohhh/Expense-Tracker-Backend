package com.tutorialseu.expensetrackerbackend.service.integration;

import com.tutorialseu.expensetrackerbackend.dto.AuthDTO;
import com.tutorialseu.expensetrackerbackend.dto.AuthResponseDTO;
import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.model.Role;
import com.tutorialseu.expensetrackerbackend.repository.UserRepository;
import com.tutorialseu.expensetrackerbackend.service.AuthService;
import com.tutorialseu.expensetrackerbackend.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AuthServiceImplIntegrationTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldSaveAndRetrieveUser(){
        AppUser appUser = new AppUser();
        appUser.setFullName("John Doe");
        appUser.setUsername("JohnDoe");
        appUser.setPassword("password123");
        appUser.setRole(Role.USER);

        userService.saveUser(appUser);

        Optional<AppUser> foundUser = userService.findUserById(appUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("JohnDoe", foundUser.get().getUsername());
    }

    @Test
    void shouldFailLoginWithInvalidCredentials(){
        AuthDTO authDTO = new AuthDTO();;
        authDTO.setUsername("JohnDoe");
        authDTO.setPassword("wrongpassword");

        AuthResponseDTO response = authService.loginUser(authDTO);

        assertEquals("invalid username or password", response.getMessage());
    }

}
