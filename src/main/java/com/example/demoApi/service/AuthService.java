package com.example.demoApi.service;

import com.example.demoApi.dto.request.IntrospectRequest;
import com.example.demoApi.dto.request.LoginRequestDTO;
import com.example.demoApi.dto.response.IntrospectResponse;
import com.example.demoApi.dto.response.LoginResponseDTO;
import com.example.demoApi.entity.User;
import com.nimbusds.jose.JOSEException;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request);
    String generateToken(User user);
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    String buildScope (User user);
}
