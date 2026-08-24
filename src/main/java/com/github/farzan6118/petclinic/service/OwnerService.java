package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.controller.dto.request.CreateOwnerRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdateOwnerRequest;
import com.github.farzan6118.petclinic.controller.dto.request.UpdateOwnerRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.OwnerResponse;
import com.github.farzan6118.petclinic.controller.dto.response.OwnerResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface OwnerService {

    OwnerResponseDto getById(UUID uuid);

    List<OwnerResponseDto> findAll();

    OwnerResponseDto create(CreateOwnerRequestDto request);

    OwnerResponseDto update(UUID uuid, UpdateOwnerRequestDto request);

    void delete(UUID uuid);
}
