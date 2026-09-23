package com.github.farzan6118.petclinic.clinic.mapper;

import com.github.farzan6118.petclinic.clinic.dto.request.CreateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.request.UpdateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.response.RoomTypeResponseDto;
import com.github.farzan6118.petclinic.clinic.model.RoomType;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RoomTypeMapper {

    public RoomTypeResponseDto mapToDto(RoomType roomType) {
        return new RoomTypeResponseDto(
                roomType.getUuid(),
                roomType.getName(),
                roomType.getDescription(),
                roomType.getEntityStatus()
        );
    }

    public void mapToEntity(CreateRoomTypeRequestDto request, RoomType roomType) {
        roomType.setName(normalizeName(request.name()));
        roomType.setDescription(request.description());
    }

    public void mapToEntity(UpdateRoomTypeRequestDto request, RoomType roomType) {
        roomType.setName(normalizeName(request.name()));
        roomType.setDescription(request.description());
    }

    private String normalizeName(String string) {
        return string.toLowerCase(Locale.ROOT).trim();
    }
}
