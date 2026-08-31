package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreatePetTypeRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdatePetTypeRequestDto;
import com.github.farzan6118.petclinic.dto.response.PetTypeResponseDto;
import com.github.farzan6118.petclinic.model.PetType;

import java.util.List;
import java.util.UUID;

public interface PetTypeService {
    PetTypeResponseDto getByUuid(UUID uuid);

    PetType getEntityByUuid(UUID uuid);

    List<PetTypeResponseDto> findAll();

    void create(CreatePetTypeRequestDto request);

    void update(UUID uuid, UpdatePetTypeRequestDto request);

    void delete(UUID uuid);

}