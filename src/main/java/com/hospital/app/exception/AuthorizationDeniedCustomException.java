package com.hospital.app.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class AuthorizationDeniedCustomException extends RuntimeException {
    private final String message;
    public AuthorizationDeniedCustomException(final String message){
        this.message = message;
    }
    public HttpStatus getHttpStatus(){
        return HttpStatus.UNAUTHORIZED;
    }
}
