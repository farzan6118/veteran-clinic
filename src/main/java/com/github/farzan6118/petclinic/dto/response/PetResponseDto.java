package com.github.farzan6118.petclinic.dto.response;

import com.github.farzan6118.petclinic.model.constant.Sex;

import java.time.LocalDate;
import java.util.UUID;

public record PetResponseDto(
        UUID uuid,
        String name,
        String color,
        String marks,
        Sex sex,
        LocalDate birthDate,
        String species
) {
}
