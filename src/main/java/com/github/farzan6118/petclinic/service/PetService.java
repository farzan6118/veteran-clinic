package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.controller.dto.request.CreatePetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdatePetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.PetResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PetService {

    PetResponseDto getById(UUID uuid);

    List<PetResponseDto> findAll();

    PetResponseDto create(CreatePetRequestDto request);

    PetResponseDto update(UUID uuid, UpdatePetRequestDto request);

    void delete(UUID uuid);
}
