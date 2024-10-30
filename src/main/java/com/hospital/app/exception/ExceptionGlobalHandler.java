package com.hospital.app.exception;

import com.hospital.app.utils.ResponseLayout;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

@ControllerAdvice
public class ExceptionGlobalHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseLayout<Object>> handler(Exception e) {
        e.printStackTrace();
        if (e instanceof ServiceException serviceException) {
            return ResponseEntity
                    .status(serviceException.getStatus())
                    .body(ResponseLayout.builder()
                            .message(serviceException.getMessage())
                            .success(false)
                            .build());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(ResponseLayout.builder()
                        .message(e.getMessage())
                        .success(false)
                        .build());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(IOException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(ResponseLayout.builder()
                        .message(e.getMessage())
                        .success(false)
                        .build());
    }

    @ExceptionHandler(BadJwtException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(BadJwtException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResponseLayout.builder()
                        .message("Token không hợp lệ")
                        .success(false)
                        .build());
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(JwtValidationException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResponseLayout.builder()
                        .message("Token đã hết hạn. Vui lòng khởi tạo token khác")
                        .success(false)
                        .build());
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(RateLimitException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ResponseLayout.builder()
                        .message(e.getMessage())
                        .success(false)
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ResponseLayout.builder()
                        .message("Xác thực không thành công. Mật khẩu không chính xác")
                        .success(false)
                        .build());
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(ServiceException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ResponseLayout.builder()
                        .message(e.getMessage())
                        .success(false)
                        .build());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(AuthorizationDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResponseLayout.builder()
                        .message("Không có quyền truy cập")
                        .success(false)
                        .build());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(NoResourceFoundException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseLayout.builder()
                        .message("Địa chỉ truy cập không chính xác")
                        .success(false)
                        .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseLayout<Object>> handler(UsernameNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseLayout.builder()
                        .message(e.getMessage())
                        .success(false)
                        .build());
    }
}
