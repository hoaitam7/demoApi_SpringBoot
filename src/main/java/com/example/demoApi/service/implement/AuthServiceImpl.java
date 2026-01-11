package com.example.demoApi.service.implement;

import com.example.demoApi.dto.request.LoginRequestDTO;
import com.example.demoApi.dto.response.LoginResponseDTO;
import com.example.demoApi.exception.AppException;
import com.example.demoApi.exception.ErrorCode;
import com.example.demoApi.repository.UserRepository;
import com.example.demoApi.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    public LoginResponseDTO login(LoginRequestDTO request)
    {
        var user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.LOGIN_FAILD);
        }
        return LoginResponseDTO.builder()
                .build();
    }
}
