package com.github.farzan6118.petclinic.room.service;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.room.dto.request.CreateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.room.dto.request.UpdateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.room.dto.response.RoomTypeResponseDto;
import com.github.farzan6118.petclinic.room.model.RoomType;

import java.util.List;
import java.util.UUID;

public interface RoomTypeService {

    RoomTypeResponseDto getByUuid(UUID uuid);

    RoomType getEntityByUuid(UUID uuid);

    List<RoomTypeResponseDto> findAll(PageAndSortRequestDto requestDto);

    void create(CreateRoomTypeRequestDto request);

    void update(UUID uuid, UpdateRoomTypeRequestDto request);

    void delete(UUID uuid);
}
