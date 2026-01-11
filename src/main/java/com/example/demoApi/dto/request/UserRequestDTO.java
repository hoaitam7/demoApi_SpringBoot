package com.example.demoApi.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @Size(min = 8, max = 100, message = "NAME_INVALID")
    private String name;
    @Size(min = 6, message = "PASSWORD_INVALID")
    private String password;
    @Email(message = "Chưa đúng định dạng email")
    private String email;
    private LocalDate birthday;

}
