package com.example.demoApi.controller;


import com.example.demoApi.dto.ApiResponse;
import com.example.demoApi.dto.request.IntrospectRequest;
import com.example.demoApi.dto.request.LoginRequestDTO;
import com.example.demoApi.dto.request.UserRequestDTO;
import com.example.demoApi.dto.response.IntrospectResponse;
import com.example.demoApi.dto.response.LoginResponseDTO;
import com.example.demoApi.dto.response.UserResponseDTO;
import com.example.demoApi.service.AuthService;
import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequestMapping("api/auth")
@RestController
@AllArgsConstructor
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {

        LoginResponseDTO result = authService.login(request);
        // Trả về theo format ApiResponse đã định nghĩa
        return ApiResponse.<LoginResponseDTO>builder()
                .code(200)
                .message("Đăng nhập thành công")
                .result(result)
                .build();
    }
    //check token
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authService.introspect(request);
        // Trả về theo format ApiResponse đã định nghĩa
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

}
