package com.hospital.app.helpers;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseLayout<T>{
    private T data;
    private String message;
    private boolean success;
}