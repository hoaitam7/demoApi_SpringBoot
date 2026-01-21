package com.example.demoApi.controller;


import com.example.demoApi.dto.ApiResponse;
import com.example.demoApi.dto.request.UserRequestDTO;
import com.example.demoApi.dto.response.UserResponseDTO;
import com.example.demoApi.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController // đánh dấu class này là controller
@RequestMapping("/api/users") //định nghĩa các url http
@AllArgsConstructor //tự động thêm các constructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponseDTO> store(@RequestBody @Valid UserRequestDTO request){
        ApiResponse<UserResponseDTO> response = new ApiResponse<>();
        response.setResult(userService.create(request));
        response.setCode(200);
        response.setMessage("Thêm user thành công");
        return response;
    }
    @GetMapping
    public ApiResponse<List<UserResponseDTO>> index(){

            var authen = SecurityContextHolder.getContext().getAuthentication();
            log.info("username: {}",  authen.getName());
            authen.getAuthorities().forEach(auth ->
                    log.info("authorities: {}",  auth.getAuthority())
                    );
        ApiResponse<List<UserResponseDTO>> response = new ApiResponse<>();
        response.setResult(userService.index());
        response.setCode(200);
        response.setMessage("Danh sách user");
        return response;
    }
    @GetMapping("/{id}")
    public ApiResponse<UserResponseDTO> show(@PathVariable int id){
        ApiResponse<UserResponseDTO> response = new ApiResponse<>();
        response.setResult(userService.show(id));
        response.setCode(200);
        response.setMessage("Lấy thông tin user thành công");
        return response;
    }

    @PutMapping("{id}")
    public ApiResponse<UserResponseDTO> update(@PathVariable("id") int id, @RequestBody UserRequestDTO request){
        ApiResponse <UserResponseDTO> response = new ApiResponse<>();
        response.setResult(userService.update(id, request));
        response.setCode(201); //created
        response.setMessage("Updated user successfully");
        return response;
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable int id){
        userService.delete(id);
        return ResponseEntity.noContent().build(); //No content
    }
}
