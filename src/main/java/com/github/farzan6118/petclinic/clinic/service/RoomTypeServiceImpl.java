package com.github.farzan6118.petclinic.clinic.service;

import com.github.farzan6118.petclinic.clinic.dto.request.CreateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.request.UpdateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.response.RoomTypeResponseDto;
import com.github.farzan6118.petclinic.clinic.mapper.RoomTypeMapper;
import com.github.farzan6118.petclinic.clinic.model.RoomType;
import com.github.farzan6118.petclinic.clinic.repository.RoomTypeRepository;
import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.common.exception.ConflictException;
import com.github.farzan6118.petclinic.common.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.common.mapper.PageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;
    private final PageMapper pageMapper;

    @Override
    public RoomTypeResponseDto getByUuid(UUID uuid) {
        RoomType roomType = getEntityByUuid(uuid);
        return roomTypeMapper.mapToDto(roomType);
    }

    @Override
    public RoomType getEntityByUuid(UUID uuid) {
        return roomTypeRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("room type not found"));
    }

    @Override
    public PageResponseDto<RoomTypeResponseDto> findAll(PageAndSortRequestDto requestDto) {
        Pageable pageable = pageMapper.getPageable(requestDto);
        Page<RoomType> roomTypePage = roomTypeRepository.findAll(pageable);
        return pageMapper.toPageResponse(roomTypePage, roomTypeMapper::mapToDto);
    }

    @Transactional
    @Override
    public void create(CreateRoomTypeRequestDto request) {
        validateNameUniqueness(request.name());
        RoomType roomType = new RoomType();
        roomTypeMapper.mapToEntity(request, roomType);
        roomTypeRepository.save(roomType);
        log.info("room type created");
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateRoomTypeRequestDto request) {
        RoomType roomType = getEntityByUuid(uuid);
        validateNameUniqueness(request.name(), uuid);
        roomTypeMapper.mapToEntity(request, roomType);
        log.info("room type updated");
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        RoomType roomType = getEntityByUuid(uuid);
        if (roomType.getEntityStatus() != EntityStatus.ACTIVE) {
            throw new ConflictException("room.type.is.inactive", "room type is already inactive");
        }
        roomType.setEntityStatus(EntityStatus.DELETED);
        log.info("room type deleted: {}", uuid);
    }

    private void validateNameUniqueness(String name) {
        String normalizedName = name.trim();
        if (roomTypeRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new ConflictException("room.type.name.exists", "room type '" + name + "' already exists");
        }
    }

    private void validateNameUniqueness(String name, UUID uuid) {
        String normalizedName = name.trim();
        if (roomTypeRepository.existsByNameIgnoreCaseAndUuidNot(normalizedName, uuid)) {
            throw new ConflictException("room.type.name.exists", "room type '" + name + "' already exists");
        }
    }
}

