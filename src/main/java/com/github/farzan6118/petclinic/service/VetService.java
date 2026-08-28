package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetResponseDto;

import java.util.List;
import java.util.UUID;

public interface VetService {

    VetResponseDto getById(UUID uuid);

    List<VetResponseDto> findAll();

    void create(CreateVetRequestDto request);

    void update(UUID uuid, UpdateVetRequestDto request);

    void delete(UUID uuid);
}
