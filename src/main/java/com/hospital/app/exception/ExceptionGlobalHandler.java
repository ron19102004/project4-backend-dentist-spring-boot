package com.hospital.app.exception;

import com.hospital.app.utils.ResponseLayout;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ResponseLayout.builder()
                        .message("Xác thực không thành công. Mật khẩu không chính xác")
                        .success(false)
                        .data(null)
                        .build());
    }
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(ServiceException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ResponseLayout.builder()
                        .message(e.getMessage())
                        .success(false)
                        .data(null)
                        .build());
    }
}
