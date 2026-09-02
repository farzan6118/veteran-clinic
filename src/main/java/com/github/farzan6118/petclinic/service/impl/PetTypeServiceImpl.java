package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreatePetTypeRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdatePetTypeRequestDto;
import com.github.farzan6118.petclinic.dto.response.PetTypeResponseDto;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.model.PetType;
import com.github.farzan6118.petclinic.repository.PetTypeRepository;
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
@Transactional(readOnly = true)
public class PetTypeServiceImpl implements PetTypeService {

    private final PetTypeRepository petTypeRepository;

    @Override
    public PetTypeResponseDto getByUuid(UUID uuid) {
        PetType petType = getEntityByUuid(uuid);
        return mapToDto(petType);
    }

    @Override
    public PetType getEntityByUuid(UUID uuid) {
        return petTypeRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("pet type not found"));
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
    public void create(CreatePetTypeRequestDto request) {
        PetType petType = new PetType();
        mapToPetType(request, petType);

        petTypeRepository.save(petType);

        log.info("Pet type created");
    }

    private void mapToPetType(CreatePetTypeRequestDto request, PetType petType) {
        petType.setName(request.name());
        petType.setCode(request.code());
        petType.setBreed(request.breed());
        petType.setOrigin(request.origin());
        petType.setDescription(request.description());
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdatePetTypeRequestDto request) {
        PetType petType = petTypeRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("pet type not found"));

        mapToPetType(request, petType);

        log.info("Pet type updated");
    }

    private void mapToPetType(UpdatePetTypeRequestDto request, PetType petType) {
        petType.setName(request.name());
        petType.setCode(request.code());
        petType.setBreed(request.breed());
        petType.setOrigin(request.origin());
        petType.setDescription(request.description());
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        PetType petType = petTypeRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("pet type not found"));

        petTypeRepository.delete(petType);

        log.info("Pet type deleted");
    }

    private PetTypeResponseDto mapToDto(PetType petType) {
        return new PetTypeResponseDto(
                petType.getUuid(),
                petType.getName(),
                petType.getCode(),
                petType.getBreed(),
                petType.getOrigin(),
                petType.getDescription()
        );
    }

}