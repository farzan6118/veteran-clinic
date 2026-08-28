package com.github.farzan6118.petclinic.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePetRequestDto(
        String name,
        LocalDate birthDate,
        String petType,
        UUID ownerUuid
) {
}
