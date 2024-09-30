package com.hospital.app.helpers;

public record ResponseLayout<T>(
        boolean status,
        T data,
        String message
) {
}
