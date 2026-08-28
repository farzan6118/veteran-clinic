package com.github.farzan6118.petclinic.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record VetResponseDto(
        UUID uuid,
        String Firstname,
        String Lastname,
        String NationalCode,
        String Telephone,
        String Email,
        String Specialty,
        LocalDate BirthDate,
        String Address,
        String City
) {
}
