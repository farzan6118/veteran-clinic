package com.github.farzan6118.petclinic.clinic.service;

import com.github.farzan6118.petclinic.clinic.dto.request.CreateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.request.UpdateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.clinic.dto.response.RoomTypeResponseDto;
import com.github.farzan6118.petclinic.clinic.mapper.RoomTypeMapper;
import com.github.farzan6118.petclinic.clinic.model.RoomType;
import com.github.farzan6118.petclinic.clinic.repository.RoomTypeRepository;
import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.common.exception.NotFoundException;
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
                .orElseThrow(() -> new NotFoundException("room type not found"));
    }

    @Override
    public List<RoomTypeResponseDto> findAll(PageAndSortRequestDto requestDto) {
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
        roomType.setEntityStatus(EntityStatus.DELETED);
        log.info("room type inactivated");
    }
}

