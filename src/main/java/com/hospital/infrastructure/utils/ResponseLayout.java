package com.hospital.infrastructure.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ResponseLayout define for response entity
 * @param <T>
 */
@Getter
@Setter
@Builder
public class ResponseLayout<T>{
    private T data;
    private String message;
    private boolean success;
}