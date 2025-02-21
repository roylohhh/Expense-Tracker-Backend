package com.tutorialseu.expensetrackerbackend.service;

import com.tutorialseu.expensetrackerbackend.dto.AppUserDTO;
import com.tutorialseu.expensetrackerbackend.dto.AuthDTO;
import com.tutorialseu.expensetrackerbackend.dto.AuthResponseDTO;
import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.model.Role;
import com.tutorialseu.expensetrackerbackend.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponseDTO registerUser(AppUserDTO appUserDTO) {
        if(userService.findByUsername(appUserDTO.getUsername()) != null){
            return new AuthResponseDTO(null, "Username is already taken");
        }

        AppUser appUser = new AppUser();
        appUser.setFullName(appUserDTO.getFullName());
        appUser.setUsername(appUserDTO.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        appUser.setRole(Role.USER);
        userService.saveUser(appUser);

        // Login user after registered
        AuthDTO authDTO = new AuthDTO();
        authDTO.setUsername(appUserDTO.getUsername());
        authDTO.setPassword(appUserDTO.getPassword());

        return loginUser(authDTO);
    }

    @Override
    public AuthResponseDTO loginUser(AuthDTO authDTO) {
        try{
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    authDTO.getUsername(),
                                    authDTO.getPassword()
                            )
                    );
            final String token = jwtUtil.generateToken(authDTO.getUsername());

            return new AuthResponseDTO(token, "success");

        } catch (BadCredentialsException e) {
            return new AuthResponseDTO(null, "invalid username or password");
        }
    }
}
