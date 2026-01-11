package com.example.demoApi.service;

import com.example.demoApi.dto.request.UserRequestDTO;
import com.example.demoApi.dto.response.UserResponseDTO;
import com.example.demoApi.entity.User;
import com.example.demoApi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<UserResponseDTO> index();
    UserResponseDTO create(UserRequestDTO userDTO);
    UserResponseDTO update(int id,  UserRequestDTO userDTO);
    void delete(int id);
    UserResponseDTO show (int id);
}
