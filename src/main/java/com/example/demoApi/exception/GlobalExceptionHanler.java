package com.example.demoApi.exception;


import com.example.demoApi.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//bắt tất cả các exception
@ControllerAdvice
public class GlobalExceptionHanler {


    //chung cho những error ngoài phạm vị
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex)
    {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED.getMessage());
        apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode()); //request
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // ex cho api với throw + api format
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> hanlingAppException(AppException ex)
    {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ex.getErrorCode(); /// lấy ra USER_EXISTED
        apiResponse.setMessage(errorCode.getMessage()); // "User already exists"
        apiResponse.setCode(errorCode.getCode()); // 400
        return ResponseEntity.badRequest().body(apiResponse);
    }

//    bắt lỗi validate dữ liệu
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex)
    {
        /// getFieldError lấy lỗi đầu tiên trong ds @Valid
        String enumKey = ex.getFieldError().getDefaultMessage(); /// lấy ra thông điệp từ annotation @Valid
        ///Nếu không tìm thấy thì mặc định là KEY_INVALID
        ErrorCode errorCode =  ErrorCode.KEY_INVALID;
        try {
             errorCode  = ErrorCode.valueOf(enumKey);

        } catch(IllegalArgumentException e ) {

        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
