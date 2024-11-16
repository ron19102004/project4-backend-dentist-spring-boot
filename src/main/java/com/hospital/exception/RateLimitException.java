package com.hospital.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
@Getter
public class RateLimitException extends RuntimeException {
    private final String message;
    private final HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
    public RateLimitException(final String message) {
        this.message = message;
    }
}
