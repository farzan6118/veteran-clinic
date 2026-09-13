package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.model.DurationTemplate;

import java.util.List;
import java.util.UUID;

public interface DurationTemplateService {
    DurationTemplateResponseDto getByUuid(UUID uuid);

    DurationTemplate getEntityByUuid(UUID uuid);

    List<DurationTemplateResponseDto> findAll();

    DurationTemplateResponseDto findByName(String name);

    DurationTemplateResponseDto findByDuration(Integer duration);

    void create(CreateDurationTemplateRequestDto request);

    void update(UUID uuid, UpdateDurationTemplateRequestDto request);

    void inactivate(UUID uuid);

    void activate(UUID uuid);

    void delete(UUID uuid);
}
