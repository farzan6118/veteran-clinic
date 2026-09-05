package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.response.RoomResponseDto;
import com.github.farzan6118.petclinic.model.Room;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    RoomResponseDto getByUuid(UUID uuid);

    Room getEntityByUuid(UUID uuid);

    List<RoomResponseDto> findAll();

    void create(CreateRoomRequestDto request);

    void update(UUID uuid, UpdateRoomRequestDto request);

    void delete(UUID uuid);

    RoomResponseDto getRoomByUuid(UUID uuid);
}
