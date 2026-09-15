package com.github.farzan6118.petclinic.owner.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record OwnerResponseDto(
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        String mobileNumber,
        String nationalId,
        LocalDate birthDate,
        String city,
        String address
) {
}
