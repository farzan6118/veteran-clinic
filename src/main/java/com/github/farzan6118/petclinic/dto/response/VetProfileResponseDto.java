package com.github.farzan6118.petclinic.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record VetProfileResponseDto(
        UUID uuid,
        String fullName,
        String nationalCode,
        String telephone,
        String email,
        String city,
        String address,
        String specialty,
        LocalDate birthDate

) {
}
