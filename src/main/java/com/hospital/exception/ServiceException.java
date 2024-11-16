package com.hospital.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceException extends RuntimeException{
   private String message;
   private Class<?> clazz;
   private HttpStatus status;
}
