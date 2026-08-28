package com.github.farzan6118.petclinic.dto.request;

import java.time.LocalDate;

public record CreateVetRequestDto(
        String firstname,
        String lastname,
        String nationalCode,
        String telephone,
        String email,
        String specialty,
        LocalDate birthDate,
        String address,
        String city
) {
}
