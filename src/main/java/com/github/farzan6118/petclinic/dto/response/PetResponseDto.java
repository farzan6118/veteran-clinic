package com.github.farzan6118.petclinic.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record PetResponseDto(
        UUID uuid,
        String name,
        LocalDate birthDate,
        String PetType
) {
}
