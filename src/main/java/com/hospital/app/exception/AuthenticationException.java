package com.hospital.app.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class AuthenticationException extends RuntimeException{
    private final String message;
    public AuthenticationException(final String message){
        this.message = message;
    }
    public HttpStatus getHttpStatus(){
        return HttpStatus.UNAUTHORIZED;
    }
}
