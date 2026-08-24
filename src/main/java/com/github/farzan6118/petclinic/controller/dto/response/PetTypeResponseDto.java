package com.github.farzan6118.petclinic.controller.dto.response;

import java.util.UUID;

public record PetTypeResponseDto(
        UUID Uuid,
        String Name
) {
}
