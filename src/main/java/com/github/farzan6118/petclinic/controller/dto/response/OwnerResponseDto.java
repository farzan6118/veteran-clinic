package com.github.farzan6118.petclinic.controller.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record OwnerResponseDto(
        UUID uuid,
        String firstname,
        String lastname,
        String nationalCode,
        LocalDate birthDate,
        String telephone,
        String email,
        String city,
        String address
) {
}
