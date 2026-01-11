package com.example.demoApi.mapper;


import com.example.demoApi.dto.request.UserRequestDTO;
import com.example.demoApi.dto.response.UserResponseDTO;
import com.example.demoApi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserRequestDTO userRequestDTO); //dto -> user
    UserResponseDTO toUserResponseDTO (User user); //user -> dto
    void updateUser (@MappingTarget User user, UserRequestDTO userRequestDTO);
}
