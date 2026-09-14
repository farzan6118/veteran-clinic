package com.github.farzan6118.petclinic.room.dto.request;

import com.github.farzan6118.petclinic.room.model.RoomType;

public record CreateRoomRequestDto(
        String name,
        String code,
        RoomType roomType,
        Boolean active
) {
}
