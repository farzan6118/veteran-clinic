package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.controller.dto.request.CreatePetTypeRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdatePetTypeRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.PetTypeResponseDto;
import com.github.farzan6118.petclinic.model.PetType;
import com.github.farzan6118.petclinic.repository.jpa.PetTypeRepository;
import com.github.farzan6118.petclinic.service.PetTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetTypeServiceImpl implements PetTypeService {

    private final PetTypeRepository petTypeRepository;

    @Override
    public PetTypeResponseDto getById(UUID uuid) {
        PetType petType = petTypeRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("pet.type.not.found"));

        return mapToDto(petType);
    }

    @Override
    public List<PetTypeResponseDto> findAll() {
        return petTypeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public PetTypeResponseDto create(CreatePetTypeRequestDto request) {
        PetType petType = new PetType();
        mapToPetType(request, petType);

        PetType savedPetType = petTypeRepository.save(petType);

        log.info("Pet type created successfully. petTypeId={}", savedPetType.getId());

        return mapToDto(savedPetType);
    }

    private void mapToPetType(CreatePetTypeRequestDto request, PetType petType) {
        petType.setName(request.name());
        petType.setCode(request.code());
        petType.setDescription(request.description());
    }

    @Transactional
    @Override
    public PetTypeResponseDto update(UUID uuid, UpdatePetTypeRequestDto request) {
        PetType petType = petTypeRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("pet.type.not.found"));

        mapToPetType(request, petType);

        log.info("Pet type updated successfully. petTypeUuid={}", uuid);

        return mapToDto(petType);
    }

    private void mapToPetType(UpdatePetTypeRequestDto request, PetType petType) {
        petType.setName(request.name());
        petType.setCode(request.code());
        petType.setDescription(request.description());
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        PetType petType = petTypeRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("pet.type.not.found"));

        petTypeRepository.delete(petType);

        log.info("Pet type deleted successfully. petTypeUuid={}", uuid);
    }

    private PetTypeResponseDto mapToDto(PetType petType) {
        return new PetTypeResponseDto(
                petType.getUuid(),
                petType.getName()
        );
    }
}