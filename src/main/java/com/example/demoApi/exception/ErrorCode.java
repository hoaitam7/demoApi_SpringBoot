package com.example.demoApi.exception;

public enum ErrorCode {
    USER_EXISTED(400, "User already exists"),
    UNCATEGORIZED(401, "Uncategorized"),
    NAME_INVALID(402, "Name is invalid, Name phải lớn hơn 8 ký tự"),
    PASSWORD_INVALID(402, "Password is invalid, Pass phải lớn hơn 6 ký tự"),
    KEY_INVALID(1000, "Invalid key"),
    USER_NOT_FOUND(400, "Không tìm thấy user"),
    LOGIN_FAILD(400,"Sai tài khoản")

    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
