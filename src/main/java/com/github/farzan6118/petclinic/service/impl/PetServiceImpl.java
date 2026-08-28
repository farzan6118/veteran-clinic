package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreatePetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdatePetRequestDto;
import com.github.farzan6118.petclinic.dto.response.PetResponseDto;
import com.github.farzan6118.petclinic.model.Owner;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.PetType;
import com.github.farzan6118.petclinic.repository.jpa.PetRepository;
import com.github.farzan6118.petclinic.service.OwnerService;
import com.github.farzan6118.petclinic.service.PetService;
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
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetTypeService petTypeService;
    private final OwnerService ownerService;

    private void mapToPet(UpdatePetRequestDto request, Owner owner, Pet pet, PetType petType) {
        pet.setName(request.name());
        pet.setBirthDate(request.birthDate());
        pet.setPetType(petType);
        pet.setOwner(owner);
    }

    private void mapToPet(CreatePetRequestDto request, Owner owner, Pet pet, PetType petType) {
        pet.setName(request.name());
        pet.setBirthDate(request.birthDate());
        pet.setPetType(petType);
        pet.setOwner(owner);
    }

    @Override
    public PetResponseDto getById(UUID uuid) {
        Pet pet = petRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("pet.not.found"));

        return mapToDto(pet);
    }

    private PetResponseDto mapToDto(Pet pet) {
        return new PetResponseDto(
                pet.getUuid(),
                pet.getName(),
                pet.getBirthDate(),
                pet.getPetType().getName()
        );
    }

    @Override
    public List<PetResponseDto> findAll() {
        return petRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void create(CreatePetRequestDto request) {
        PetType petType = petTypeService.getByPetType(request.petType());
        Owner owner = ownerService.getByUuid(request.ownerUuid());
        Pet pet = new Pet();
        mapToPet(request, owner, pet, petType);

        Pet savedPet = petRepository.save(pet);

        log.info("Pet created successfully. petId={}", savedPet.getId());
    }

    private void mapToPet(CreatePetRequestDto request, Pet pet, PetType petType) {
        pet.setName(request.name());
        pet.setBirthDate(request.birthDate());
        pet.setPetType(petType);
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdatePetRequestDto request) {
        Pet pet = this.getByUuid(uuid);
        PetType petType = petTypeService.getByPetType(request.petType());
        Owner owner = ownerService.getByUuid(request.ownerUuid());
        mapToPet(request, owner, pet, petType);

        log.info("Pet updated successfully. petUuid={}", uuid);
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Pet pet = this.getByUuid(uuid);

        petRepository.delete(pet);

        log.info("Pet deleted successfully. petUuid={}", uuid);
    }

    @Override
    public Pet getByUuid(UUID uuid) {
        return petRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("pet.not.found"));
    }
}

