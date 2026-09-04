package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreateOwnerRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateOwnerRequestDto;
import com.github.farzan6118.petclinic.dto.response.OwnerResponseDto;
import com.github.farzan6118.petclinic.model.Owner;

import java.util.List;
import java.util.UUID;

public interface OwnerService {
    OwnerResponseDto getByUuid(UUID uuid);

    List<OwnerResponseDto> findAll();

    void create(CreateOwnerRequestDto request);

    void update(UUID uuid, UpdateOwnerRequestDto request);

    void delete(UUID uuid);

    Owner getEntityByUuid(UUID uuid);
}
