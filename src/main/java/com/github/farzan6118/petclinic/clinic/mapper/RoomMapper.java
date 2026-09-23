package com.github.farzan6118.petclinic.clinic.mapper;

import com.github.farzan6118.petclinic.clinic.dto.request.CreateRoomRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.request.UpdateRoomRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.response.RoomResponseDto;
import com.github.farzan6118.petclinic.clinic.model.Room;
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
                room.getRoomNumber(),
                roomTypeMapper.mapToDto(room.getRoomType()),
                room.isActive()
        );
    }

    public void mapToEntity(CreateRoomRequestDto request, Room room) {
        room.setName(normalizeName(request.name()));
        room.setRoomNumber(normalizeName(request.code()));
        room.setRoomType(request.roomType());
        room.setActive(request.active());
    }

    public void mapToEntity(UpdateRoomRequestDto request, Room room) {
        room.setName(normalizeName(request.name()));
        room.setRoomNumber(normalizeName(request.code()));
        room.setRoomType(request.roomType());
        room.setActive(request.active());
    }

    private String normalizeName(String string) {
        return string.toLowerCase(Locale.ROOT).trim();
    }
}
