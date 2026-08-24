package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.controller.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.VetResponseDto;

import java.util.List;
import java.util.UUID;

public interface VetService {

    VetResponseDto getById(UUID uuid);

    List<VetResponseDto> findAll();

    VetResponseDto create(CreateVetRequestDto request);

    VetResponseDto update(UUID uuid, UpdateVetRequestDto request);

    void delete(UUID uuid);
}
