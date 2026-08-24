package com.github.farzan6118.petclinic.controller.dto.request;

import java.time.LocalDate;

public record UpdateOwnerRequestDto(
        String firstname,
        String lastname,
        String address,
        String city,
        String telephone,
        String nationalCode,
        LocalDate birthDate,
        String email
) {
}
