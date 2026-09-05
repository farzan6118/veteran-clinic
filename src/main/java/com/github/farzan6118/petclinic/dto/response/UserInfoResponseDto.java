package com.github.farzan6118.petclinic.dto.response;

public record UserInfoResponseDto(
        String givenName,
        String familyName,
        String email,
        String MobileNumber,
        String nationalId
) {
}
