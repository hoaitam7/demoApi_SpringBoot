package com.example.demoApi.service;

import com.example.demoApi.dto.request.LoginRequestDTO;
import com.example.demoApi.dto.response.LoginResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request);
    String generateToken(String name);
}
