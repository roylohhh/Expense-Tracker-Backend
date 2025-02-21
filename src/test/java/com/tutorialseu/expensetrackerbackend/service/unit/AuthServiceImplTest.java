package com.tutorialseu.expensetrackerbackend.service.unit;

import com.tutorialseu.expensetrackerbackend.dto.AppUserDTO;
import com.tutorialseu.expensetrackerbackend.dto.AuthDTO;
import com.tutorialseu.expensetrackerbackend.dto.AuthResponseDTO;
import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.model.Role;
import com.tutorialseu.expensetrackerbackend.service.AuthServiceImpl;
import com.tutorialseu.expensetrackerbackend.service.UserService;
import com.tutorialseu.expensetrackerbackend.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private AppUserDTO appUserDTO;
    private AuthDTO authDTO;
    private AppUser appUser;

    @BeforeEach
    void setUp(){
        appUserDTO = new AppUserDTO();
        appUserDTO.setFullName("John Doe");
        appUserDTO.setUsername("JohnDoe");
        appUserDTO.setPassword("password1");

        authDTO = new AuthDTO();
        authDTO.setUsername("JohnDoe");
        authDTO.setPassword("password1");

        appUser = new AppUser();
        appUser.setFullName("John Doe");
        appUser.setUsername("John Doe");
        appUser.setPassword("password1");
        appUser.setRole(Role.USER);
    }

    @Test
    void testRegisterUser_Success(){
        when(userService.findByUsername("JohnDoe")).thenReturn(null);
        when(passwordEncoder.encode("password1")).thenReturn("encodedPassword");
        when(userService.saveUser(any(AppUser.class))).thenReturn(appUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("JohnDoe", "password1"));
        when(jwtUtil.generateToken("JohnDoe")).thenReturn("mockedToken");

        AuthResponseDTO response = authService.registerUser(appUserDTO);

        assertNotNull(response);
        assertEquals("mockedToken", response.getToken());
        assertEquals("success", response.getMessage());

        verify(userService, times(1)).saveUser(any(AppUser.class));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRegisterUser_Failure_UsernameToken(){
        when(userService.findByUsername("JohnDoe")).thenReturn(appUser);

        AuthResponseDTO response = authService.registerUser(appUserDTO);

        assertNull(response.getToken());
        assertEquals("Username is already taken", response.getMessage());

        verify(userService, never()).saveUser(any(AppUser.class));
        verify(authenticationManager, never())
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLoginUser_Success(){
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("JohnDoe", "password1"));
        when(jwtUtil.generateToken("JohnDoe")).thenReturn("mockedToken");

        AuthResponseDTO response = authService.loginUser(authDTO);

        assertNotNull(response);
        assertEquals("mockedToken", response.getToken());
        assertEquals("success", response.getMessage());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken("JohnDoe");
    }

    @Test
    void testLoginUser_Failure_InvalidCredentials(){
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        AuthResponseDTO response = authService.loginUser(authDTO);

        assertNull(response.getToken());
        assertEquals("invalid username or password", response.getMessage());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(anyString());
    }

}
