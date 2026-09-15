package com.github.farzan6118.petclinic.visit.service;

import com.github.farzan6118.petclinic.visit.dto.request.CreateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.UpdateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.visit.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.visit.model.DurationTemplate;

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
