package com.github.farzan6118.petclinic.pet.service;

import com.github.farzan6118.petclinic.common.dto.request.PageRequestDto;
import com.github.farzan6118.petclinic.common.dto.request.SortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.pet.dto.request.CreateSpeciesRequestDto;
import com.github.farzan6118.petclinic.pet.dto.request.UpdateSpeciesRequestDto;
import com.github.farzan6118.petclinic.pet.dto.response.SpeciesResponseDto;
import com.github.farzan6118.petclinic.pet.model.Species;

import java.util.List;
import java.util.UUID;

public interface SpeciesService {
    SpeciesResponseDto getByUuid(UUID uuid);

    Species getEntityByUuid(UUID uuid);

    PageResponseDto<SpeciesResponseDto> findAll(PageRequestDto page, SortRequestDto sort);

    List<SpeciesResponseDto> findAll();

    void create(CreateSpeciesRequestDto request);

    void update(UUID uuid, UpdateSpeciesRequestDto request);

    void delete(UUID uuid);

}