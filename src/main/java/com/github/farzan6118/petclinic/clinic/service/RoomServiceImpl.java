package com.github.farzan6118.petclinic.clinic.service;

import com.github.farzan6118.petclinic.clinic.dto.request.CreateRoomRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.request.UpdateRoomRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.response.RoomResponseDto;
import com.github.farzan6118.petclinic.clinic.mapper.RoomMapper;
import com.github.farzan6118.petclinic.clinic.model.Room;
import com.github.farzan6118.petclinic.clinic.repository.RoomRepository;
import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.common.enums.VisitCategory;
import com.github.farzan6118.petclinic.common.enums.VisitType;
import com.github.farzan6118.petclinic.common.exception.NotFoundException;
import com.github.farzan6118.petclinic.common.exception.ValidationException;
import com.github.farzan6118.petclinic.common.mapper.PageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final PageMapper pageMapper;

    @Override
    public RoomResponseDto getByUuid(UUID uuid) {
        Room room = getEntityByUuid(uuid);
        return roomMapper.mapToDto(room);
    }

    @Override
    public Room getEntityByUuid(UUID uuid) {
        return roomRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("room not found"));
    }

    @Override
    public PageResponseDto<RoomResponseDto> findAll(PageAndSortRequestDto requestDto) {
        Pageable pageable = pageMapper.getPageable(requestDto);
        Page<Room> roomPage = roomRepository.findAll(pageable);
        return pageMapper.toPageResponse(roomPage, roomMapper::mapToDto);
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
        if (roomRepository.existsByRoomNumber(code)) {
            throw new ValidationException("room exists", "room code'" + code + "' exists");
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
        if (roomRepository.existsByRoomNumberAndUuidNot(code, roomUuid)) {
            throw new ValidationException("Room with this email already exists");
        }
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Room room = this.getEntityByUuid(uuid);
        if (!room.getEntityStatus().equals(EntityStatus.ACTIVE)) {
            throw new ValidationException("room is already inactive");
        }
        room.setEntityStatus(EntityStatus.DELETED);
        log.info("room inactivated");
    }

    @Override
    public RoomResponseDto getRoomByUuid(UUID uuid) {
        Room room = getEntityByUuid(uuid);
        return roomMapper.mapToDto(room);
    }

    @Override
    public Room getAvailableRoomByVisitTypeAndVisitCategory(VisitType visitType, VisitCategory visitCategory) {
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
                .orElseThrow(() -> new NotFoundException(
                        "visit.room.not.available",
                        "No room is available for the selected visit type and time"));
    }
}

