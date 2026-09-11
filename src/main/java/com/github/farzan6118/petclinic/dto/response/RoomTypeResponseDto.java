package com.github.farzan6118.petclinic.dto.response;

import com.github.farzan6118.petclinic.model.constant.EntityStatus;

import java.util.UUID;

public record RoomTypeResponseDto(
        UUID uuid,
        String name,
        String description,
        EntityStatus status
) {
}
