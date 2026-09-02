package com.github.farzan6118.petclinic.dto.response;

import java.util.UUID;

public record PetTypeResponseDto(
        UUID uuid,
        String name,
        String code,
        String breed,
        String origin,
        String description
) {
}
