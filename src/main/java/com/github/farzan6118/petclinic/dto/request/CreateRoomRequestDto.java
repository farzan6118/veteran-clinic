package com.github.farzan6118.petclinic.dto.request;

import com.github.farzan6118.petclinic.model.RoomType;

public record CreateRoomRequestDto(
        String name,
        String code,
        RoomType roomType,
        Boolean active
) {
}
