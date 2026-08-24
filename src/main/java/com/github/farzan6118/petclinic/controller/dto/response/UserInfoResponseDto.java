package com.github.farzan6118.petclinic.controller.dto.response;

public record UserInfoResponseDto(
        String givenName,
        String familyName,
        String email,
        String phoneNumber,
        String nationalCode
) {
}
