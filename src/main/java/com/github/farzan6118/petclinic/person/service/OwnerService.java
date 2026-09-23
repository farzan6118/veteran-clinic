package com.github.farzan6118.petclinic.person.service;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.owner.model.Owner;
import com.github.farzan6118.petclinic.person.dto.request.CreateOwnerRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.UpdatePersonRequestDto;
import com.github.farzan6118.petclinic.person.dto.response.PersonResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface OwnerService {
    PersonResponseDto getByUuid(UUID uuid);

    PageResponseDto<PersonResponseDto> findAll(PageAndSortRequestDto requestDto);

    void create(CreateOwnerRequestDto request);

    void update(UUID uuid, UpdatePersonRequestDto request);

    @Transactional
    void inactivate(UUID uuid);

    @Transactional
    void activate(UUID uuid);

    void delete(UUID uuid);

    Owner getEntityByUuid(UUID uuid);
}
