package com.tutorialseu.expensetrackerbackend.service.unit;

import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.repository.UserRepository;
import com.tutorialseu.expensetrackerbackend.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private AppUser testUser;

    @BeforeEach
    void setUp(){
        testUser = new AppUser();
        testUser.setId(1L);
        testUser.setUsername("JohnDoe");
        testUser.setPassword("password1");
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);

        AppUser savedUser = userService.saveUser(testUser);

        assertNotNull(savedUser);
        assertEquals("JohnDoe", savedUser.getUsername());
        verify(userRepository, times(1)).save(testUser); // Ensure save() was called once
    }

    @Test
    void testFindByUsername_UserExists(){
        when(userRepository.findByUsername("JohnDoe")).thenReturn(Optional.of(testUser));

        AppUser appUser = userService.findByUsername("JohnDoe");
        assertNotNull(appUser);
        assertEquals("JohnDoe", appUser.getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound(){
        when(userRepository.findByUsername("RoyLoh")).thenReturn(Optional.empty());

        AppUser appUser = userService.findByUsername("RoyLoh");
        assertNull(appUser); // Should return null when user is not found
    }

    @Test
    void testFindById(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Optional<AppUser> appUser = userService.findUserById(1L);

        assertTrue(appUser.isPresent());
        assertEquals(1L,appUser.get().getId());
    }
}
