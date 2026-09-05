package com.github.farzan6118.petclinic.dto.response;

import java.util.UUID;

public record RoomTypeResponseDto(
        UUID uuid,
        String name,
        String description
) {
}
