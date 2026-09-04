package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreateSpeciesRequestDto;
import com.github.farzan6118.petclinic.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateSpeciesRequestDto;
import com.github.farzan6118.petclinic.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.dto.response.SpeciesResponseDto;
import com.github.farzan6118.petclinic.model.Species;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SpeciesService {
    SpeciesResponseDto getByUuid(UUID uuid);

    Species getEntityByUuid(UUID uuid);

    PageResponseDto<SpeciesResponseDto> findAll(PageAndSortRequestDto pageable);

    List<SpeciesResponseDto> findAll();

    void create(CreateSpeciesRequestDto request);

    void update(UUID uuid, UpdateSpeciesRequestDto request);

    void delete(UUID uuid);

}