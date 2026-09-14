package com.github.farzan6118.petclinic.pet.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.farzan6118.petclinic.common.enums.Sex;

import java.time.LocalDate;
import java.util.UUID;

public record PetResponseDto(
        UUID uuid,
        String name,
        String color,
        String marks,
        Sex sex,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate,
        String species
) {
}
