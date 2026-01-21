package com.example.demoApi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Integer id;
    private String name;
//    private String password;
    private String email;
    private LocalDate birthday;
    private Set<String> roles;

}
