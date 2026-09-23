package com.github.farzan6118.petclinic.clinic.dto.request;

import com.github.farzan6118.petclinic.clinic.model.RoomType;

public record CreateRoomRequestDto(
        String name,
        String code,
        RoomType roomType,
        Boolean active
) {
}
