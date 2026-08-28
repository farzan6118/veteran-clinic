package com.github.farzan6118.petclinic.dto.response;

import java.util.UUID;

public record PetTypeResponseDto(
        UUID Uuid,
        String Name,
        String code,
        String description
) {
}
