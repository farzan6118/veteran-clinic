package com.github.farzan6118.petclinic.auth.dto.response;

public record UserInfoResponseDto(
        String givenName,
        String familyName,
        String email,
        String MobileNumber,
        String nationalId
) {
}
