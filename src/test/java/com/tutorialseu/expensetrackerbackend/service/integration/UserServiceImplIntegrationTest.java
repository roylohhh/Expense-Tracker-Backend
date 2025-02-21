package com.tutorialseu.expensetrackerbackend.service.integration;

import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.model.Role;
import com.tutorialseu.expensetrackerbackend.repository.UserRepository;
import com.tutorialseu.expensetrackerbackend.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserServiceImplIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser() {
        AppUser user = new AppUser();
        user.setUsername("JohnDoe");
        user.setPassword("password123");
        user.setRole(Role.USER);

        userService.saveUser(user);

        Optional<AppUser> foundUser = userService.findUserById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("JohnDoe", foundUser.get().getUsername());
    }
}
