package com.github.farzan6118.petclinic.person.dto.response;

import java.util.UUID;

public record PersonResponseDto(
        UUID uuid,
        String title,
        String firstName,
        String lastName,
        String nationalId,
        AddressResponseDto address,
        ProfileResponseDto profile
) {
}
