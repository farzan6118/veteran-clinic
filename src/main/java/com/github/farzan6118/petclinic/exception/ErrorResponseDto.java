package com.github.farzan6118.petclinic.exception;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        int status,
        String error,
        String message,
        LocalDateTime timestamp,
        String path
) {
}