package com.example.demoApi.service.implement;

import com.example.demoApi.dto.request.UserRequestDTO;
import com.example.demoApi.dto.response.UserResponseDTO;
import com.example.demoApi.entity.User;
import com.example.demoApi.exception.AppException;
import com.example.demoApi.exception.ErrorCode;
import com.example.demoApi.mapper.UserMapper;
import com.example.demoApi.repository.UserRepository;
import com.example.demoApi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDTO> index() {

        List<User> users = userRepository.findAll();
        //Chuyển đổi Entity sang DTO
        return users.stream()
                .map(userMapper::toUserResponseDTO)
                .toList();
    }
    @Override
    public UserResponseDTO create(UserRequestDTO request) {

        if(userRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request); //dto -> model
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); //độ phức tạp : 10
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user =  userRepository.save(user); // save
        return userMapper.toUserResponseDTO(user); //dto
    }
    @Override
    public UserResponseDTO update (int id, UserRequestDTO request)
    {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found with id " + id));
        userMapper.updateUser(user, request);
        User updateUser = userRepository.save(user);
        return userMapper.toUserResponseDTO(updateUser);
    }
    @Override
    public void delete(int id) {
        if (!userRepository.existsById(id))
        {
            throw new RuntimeException("User Not found");
        }
        userRepository.deleteById(id);
    }
    @Override
    public UserResponseDTO show (int id) {
        User user = userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponseDTO(user);
    }
}
