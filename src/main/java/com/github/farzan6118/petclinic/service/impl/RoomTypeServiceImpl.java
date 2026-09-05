package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.dto.response.RoomTypeResponseDto;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.mapper.RoomTypeMapper;
import com.github.farzan6118.petclinic.model.RoomType;
import com.github.farzan6118.petclinic.model.constant.EntityStatus;
import com.github.farzan6118.petclinic.repository.RoomTypeRepository;
import com.github.farzan6118.petclinic.service.RoomTypeService;
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
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;

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
    public List<RoomTypeResponseDto> findAll() {
        return roomTypeRepository.findAll()
                .stream()
                .map(roomTypeMapper::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public void create(CreateRoomTypeRequestDto request) {
        RoomType roomType = new RoomType();
        roomTypeMapper.mapToEntity(request, roomType);
        roomTypeRepository.save(roomType);
        log.info("room type created");
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateRoomTypeRequestDto request) {
        RoomType roomType = getEntityByUuid(uuid);
        roomTypeMapper.mapToEntity(request, roomType);
        log.info("room type updated");
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        RoomType roomType = getEntityByUuid(uuid);
        roomType.setEntityStatus(EntityStatus.INACTIVE_DELETED);
        log.info("room type inactivated");
    }
}

