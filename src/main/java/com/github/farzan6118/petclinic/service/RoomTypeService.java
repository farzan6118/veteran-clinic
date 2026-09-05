package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.dto.response.RoomTypeResponseDto;
import com.github.farzan6118.petclinic.model.RoomType;

import java.util.List;
import java.util.UUID;

public interface RoomTypeService {

    RoomTypeResponseDto getByUuid(UUID uuid);

    RoomType getEntityByUuid(UUID uuid);

    List<RoomTypeResponseDto> findAll();

    void create(CreateRoomTypeRequestDto request);

    void update(UUID uuid, UpdateRoomTypeRequestDto request);

    void delete(UUID uuid);
}
