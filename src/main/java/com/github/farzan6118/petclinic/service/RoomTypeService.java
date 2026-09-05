package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.VetProfileUpdateRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetProfileResponseDto;
import com.github.farzan6118.petclinic.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.model.Vet;

import java.util.List;
import java.util.UUID;

public interface RoomTypeService {

    VetResponseDto getByUuid(UUID uuid);

    Vet getEntityByUuid(UUID uuid);

    List<VetResponseDto> findAll();

    void create(CreateVetRequestDto request);

    void updateVetProfileByUuid(VetProfileUpdateRequestDto request, UUID vetUuid);

    void update(UUID uuid, UpdateVetRequestDto request);

    void delete(UUID uuid);

    VetProfileResponseDto getVetProfileByUuid(UUID uuid);
}
