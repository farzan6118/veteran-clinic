package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreatePetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdatePetRequestDto;
import com.github.farzan6118.petclinic.dto.response.PetResponseDto;
import com.github.farzan6118.petclinic.model.Pet;

import java.util.List;
import java.util.UUID;

public interface PetService {

    PetResponseDto getById(UUID uuid);

    List<PetResponseDto> findAll();

    void create(CreatePetRequestDto request);

    void update(UUID uuid, UpdatePetRequestDto request);

    void delete(UUID uuid);

    Pet getByUuid(UUID uuid);
}
