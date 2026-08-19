package com.github.farzan6118.petclinic.controller.dto.response;

public record LoginResponse(
        String accessToken,
        long accessExpiresIn,
        String refreshToken,
        long refreshExpiresIn
) {
}