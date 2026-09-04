package com.github.farzan6118.petclinic.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record OwnerResponseDto(
        UUID uuid,
        String firstname,
        String lastname,
        String email,
        String mobileNumber,
        String nationalCode,
        LocalDate birthDate,
        String city,
        String address
) {
}
