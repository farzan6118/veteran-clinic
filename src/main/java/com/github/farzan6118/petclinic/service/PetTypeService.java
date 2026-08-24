package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.controller.dto.request.CreatePetTypeRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdatePetTypeRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.PetTypeResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PetTypeService {
    PetTypeResponseDto getById(UUID uuid);

    List<PetTypeResponseDto> findAll();

    @Transactional
    PetTypeResponseDto create(CreatePetTypeRequestDto request);

    @Transactional
    PetTypeResponseDto update(
            UUID uuid,
            UpdatePetTypeRequestDto request
    );

    @Transactional
    void delete(UUID uuid);
}