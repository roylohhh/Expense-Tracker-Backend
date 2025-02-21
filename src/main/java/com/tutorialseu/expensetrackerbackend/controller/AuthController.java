package com.tutorialseu.expensetrackerbackend.controller;

import com.tutorialseu.expensetrackerbackend.dto.AppUserDTO;
import com.tutorialseu.expensetrackerbackend.dto.AuthDTO;
import com.tutorialseu.expensetrackerbackend.dto.AuthResponseDTO;
import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody AppUserDTO appUserDTO){
        AuthResponseDTO response = authService.registerUser(appUserDTO);
        if("success".equals(response.getMessage())){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO>login(@RequestBody AuthDTO authDTO){
        AuthResponseDTO response = authService.loginUser(authDTO);
        if("success".equals(response.getMessage())){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
