package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.response.RoomResponseDto;
import com.github.farzan6118.petclinic.exception.GenericValidationException;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.mapper.RoomMapper;
import com.github.farzan6118.petclinic.model.Room;
import com.github.farzan6118.petclinic.model.constant.EntityStatus;
import com.github.farzan6118.petclinic.model.constant.VisitType;
import com.github.farzan6118.petclinic.repository.RoomRepository;
import com.github.farzan6118.petclinic.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public RoomResponseDto getByUuid(UUID uuid) {
        Room room = getEntityByUuid(uuid);
        return roomMapper.mapToDto(room);
    }

    @Override
    public Room getEntityByUuid(UUID uuid) {
        return roomRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("room not found"));
    }

    @Override
    public List<RoomResponseDto> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public void create(CreateRoomRequestDto request) {

        validateUniqueContactInfo(request.code());
        Room room = new Room();
        roomMapper.mapToEntity(request, room);

        roomRepository.save(room);
        log.info("room created");

    }

    private void validateUniqueContactInfo(String code) {
        if (roomRepository.existsByCode(code)) {
            throw new GenericValidationException("room exists", "room code'" + code + "' exists");
        }
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateRoomRequestDto request) {
        Room room = getEntityByUuid(uuid);
        validateCodeUniqueness(request.code(), uuid);
        roomMapper.mapToEntity(request, room);
        log.info("room updated");
    }

    private void validateCodeUniqueness(String code, UUID roomUuid) {
        if (roomRepository.existsByCodeAndUuidNot(code, roomUuid)) {
            throw new GenericValidationException("Room with this email already exists");
        }
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Room room = this.getEntityByUuid(uuid);
        if (!room.getEntityStatus().equals(EntityStatus.ACTIVE)) {
            throw new GenericValidationException("room is already inactive");
        }
        room.setEntityStatus(EntityStatus.INACTIVE_DELETED);
        log.info("room inactivated");
    }

    @Override
    public RoomResponseDto getRoomByUuid(UUID uuid) {
        Room room = getEntityByUuid(uuid);
        return roomMapper.mapToDto(room);
    }

    @Override
    public Room allocateRoom(VisitType visitType) {
        List<String> roomTypeNames = switch (visitType) {
            case ONSITE -> List.of("examination", "individual");
            case ONLINE, OFFSITE -> List.of();
        };

        if (roomTypeNames.isEmpty()) {
            return null;
        }

        return roomRepository.findActiveRoomsByTypeNames(roomTypeNames)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "visit.room.not.available",
                        "No room is available for the selected visit type and time"));
    }
}

