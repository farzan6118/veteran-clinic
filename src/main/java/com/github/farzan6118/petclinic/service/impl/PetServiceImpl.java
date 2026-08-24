package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.controller.dto.request.CreatePetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdatePetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.PetResponseDto;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.PetType;
import com.github.farzan6118.petclinic.repository.jpa.PetRepository;
import com.github.farzan6118.petclinic.repository.jpa.PetTypeRepository;
import com.github.farzan6118.petclinic.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;

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

    @Transactional
    @Override
    public PetResponseDto create(CreatePetRequestDto request) {
        PetType petType = getPetType(request.petType());
        Pet pet = new Pet();
        mapToPet(request, pet, petType);

        Pet savedPet = petRepository.save(pet);

        log.info("Pet created successfully. petId={}", savedPet.getId());

        return mapToDto(savedPet);
    }

    private void mapToPet(CreatePetRequestDto request, Pet pet, PetType petType) {
        pet.setName(request.name());
        pet.setBirthDate(request.birthDate());
        pet.setPetType(petType);
    }

    private PetType getPetType(String petType) {
        List<PetType> petTypesByName = petTypeRepository.findByName(petType);
        if(petTypesByName.isEmpty()){
            throw new RuntimeException("petType.not.found");
        }
        return petTypesByName.getFirst();
    }

    @Transactional
    @Override
    public PetResponseDto update(UUID uuid, UpdatePetRequestDto request) {
        Pet pet = petRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("pet.not.found"));
        PetType petType = getPetType(request.petType());
        mapToPet(request, pet, petType);

        log.info("Pet updated successfully. petUuid={}", uuid);

        return mapToDto(pet);
    }

    private static void mapToPet(UpdatePetRequestDto request, Pet pet, PetType petType) {
        pet.setName(request.name());
        pet.setBirthDate(request.birthDate());
        pet.setPetType(petType);
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Pet pet = petRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("pet.not.found"));

        petRepository.delete(pet);

        log.info("Pet deleted successfully. petUuid={}", uuid);
    }
}

