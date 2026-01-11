package com.example.demoApi.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//fomat api trả vể client
@JsonInclude(JsonInclude.Include.NON_NULL)
//tự động loại bỏ các field null khỏi response
//chuyển đổi object java thành json
public class ApiResponse <T> {
    private int code; //mã
    private String message;
    private T result;
}

