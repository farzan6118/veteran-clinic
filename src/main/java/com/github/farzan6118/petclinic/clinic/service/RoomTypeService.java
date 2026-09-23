package com.github.farzan6118.petclinic.clinic.service;

import com.github.farzan6118.petclinic.clinic.dto.request.CreateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.request.UpdateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.response.RoomTypeResponseDto;
import com.github.farzan6118.petclinic.clinic.model.RoomType;
import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;

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
