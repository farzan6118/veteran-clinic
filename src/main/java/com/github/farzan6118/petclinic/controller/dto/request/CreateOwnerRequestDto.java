package com.github.farzan6118.petclinic.controller.dto.request;

import java.time.LocalDate;

public record CreateOwnerRequestDto(
        String firstname,
        String lastname,
        String nationalCode,
        String address,
        String city,
        String telephone,
        LocalDate birthDate,
        String email
) {
}
