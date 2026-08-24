package com.github.farzan6118.petclinic.controller.dto.request;

import java.time.LocalDate;

public record UpdatePetRequestDto(
        String name,
        LocalDate birthDate,
        String petType
) {
}
