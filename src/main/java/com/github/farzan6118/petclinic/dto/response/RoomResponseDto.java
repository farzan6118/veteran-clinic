package com.github.farzan6118.petclinic.dto.response;

import com.github.farzan6118.petclinic.model.RoomType;

import java.util.UUID;

public record RoomResponseDto(
        UUID uuid,
        String name,
        String code,
        RoomType roomType,
        Boolean active
) {
}
