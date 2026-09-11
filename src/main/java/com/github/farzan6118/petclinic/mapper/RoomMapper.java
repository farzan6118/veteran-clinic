package com.github.farzan6118.petclinic.mapper;

import com.github.farzan6118.petclinic.dto.request.CreateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.response.RoomResponseDto;
import com.github.farzan6118.petclinic.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RoomMapper {

    private final RoomTypeMapper roomTypeMapper;

    public RoomResponseDto mapToDto(Room room) {
        return new RoomResponseDto(
                room.getUuid(),
                room.getName(),
                room.getCode(),
                roomTypeMapper.mapToDto(room.getRoomType()),
                room.isActive()
        );
    }

    public void mapToEntity(CreateRoomRequestDto request, Room room) {
        room.setName(normalizeName(request.name()));
        room.setCode(normalizeName(request.code()));
        room.setRoomType(request.roomType());
        room.setActive(request.active());
    }

    public void mapToEntity(UpdateRoomRequestDto request, Room room) {
        room.setName(normalizeName(request.name()));
        room.setCode(normalizeName(request.code()));
        room.setRoomType(request.roomType());
        room.setActive(request.active());
    }

    private String normalizeName(String string) {
        return string.toLowerCase(Locale.ROOT).trim();
    }
}
