package com.github.farzan6118.petclinic.room.service;

import com.github.farzan6118.petclinic.common.enums.VisitCategory;
import com.github.farzan6118.petclinic.common.enums.VisitType;
import com.github.farzan6118.petclinic.room.dto.request.CreateRoomRequestDto;
import com.github.farzan6118.petclinic.room.dto.request.UpdateRoomRequestDto;
import com.github.farzan6118.petclinic.room.dto.response.RoomResponseDto;
import com.github.farzan6118.petclinic.room.model.Room;

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

    Room getAvailableRoomByVisitTypeAndVisitCategory(VisitType visitType, VisitCategory visitCategory);
}
