package com.tutorialseu.expensetrackerbackend.service;

import com.tutorialseu.expensetrackerbackend.dto.AppUserDTO;
import com.tutorialseu.expensetrackerbackend.dto.AuthDTO;
import com.tutorialseu.expensetrackerbackend.dto.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO registerUser(AppUserDTO appUserDTO);
    AuthResponseDTO loginUser(AuthDTO authDTO);
}
