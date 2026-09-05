package com.github.farzan6118.petclinic.mapper;

import com.github.farzan6118.petclinic.dto.request.CreateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.response.RoomTypeResponseDto;
import com.github.farzan6118.petclinic.model.Room;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RoomTypeMapper {

    public RoomTypeResponseDto mapToDto(Room room) {
        return new RoomTypeResponseDto(
                room.getUuid(),
                room.getCode(),
                room.getName(),
                room.isActive()
        );
    }

    public void mapToEntity(CreateRoomRequestDto request, Room room) {
        room.setName(normalizeName(request.name()));
        room.setRoomType(request.roomType());
        room.setActive(request.active());
    }

    public void mapToEntity(UpdateRoomRequestDto request, Room room) {
        room.setName(normalizeName(request.name()));
        room.setRoomType(request.roomType());
        room.setActive(request.active());
    }

    private String normalizeName(String string) {
        return string.toLowerCase(Locale.ROOT).trim();
    }
}
