package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreatePetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdatePetRequestDto;
import com.github.farzan6118.petclinic.dto.response.PetResponseDto;
import com.github.farzan6118.petclinic.exception.GenericValidationException;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.mapper.PetMapper;
import com.github.farzan6118.petclinic.model.Owner;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Species;
import com.github.farzan6118.petclinic.model.constant.EntityStatus;
import com.github.farzan6118.petclinic.repository.PetRepository;
import com.github.farzan6118.petclinic.service.OwnerService;
import com.github.farzan6118.petclinic.service.PetService;
import com.github.farzan6118.petclinic.service.SpeciesService;
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
    private final SpeciesService speciesService;
    private final OwnerService ownerService;
    private final PetMapper petMapper;

    @Override
    public PetResponseDto getByUuid(UUID uuid) {
        Pet pet = this.getEntityByUuid(uuid);
        return petMapper.mapToDto(pet);
    }


    @Override
    public List<PetResponseDto> findAll() {
        return petRepository.findAll()
                .stream()
                .map(petMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void create(CreatePetRequestDto request) {
        Owner owner = ownerService.getEntityByUuid(request.ownerUuid());
        validateUniqueness(owner.getId(), request.name());
        Species species = speciesService.getEntityByUuid(request.speciesUuid());
        Pet pet = new Pet();
        petMapper.mapToPet(request, owner, pet, species);
        petRepository.save(pet);
        log.info("pet created");
    }

    private void validateUniqueness(Long ownerId, String name) {
        if (petRepository.existsByOwnerIdAndNameIgnoreCase(ownerId, name)) {
            throw new GenericValidationException("owner's pet already exists");
        }
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdatePetRequestDto request) {
        Pet pet = this.getEntityByUuid(uuid);
        Owner owner = ownerService.getEntityByUuid(request.ownerUuid());
        validateUniqueness(owner.getId(), request.name(), pet.getId());
        Species species = speciesService.getEntityByUuid(request.speciesUuid());
        petMapper.mapToPet(request, owner, pet, species);
        log.info("pet updated");
    }

    private void validateUniqueness(Long ownerId, String name, Long petId) {
        if (petRepository.existsByOwnerIdAndNameIgnoreCaseAndIdNot(ownerId, name, petId)) {
            throw new GenericValidationException("owner's pet already exists");
        }
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Pet pet = this.getEntityByUuid(uuid);
        if(!pet.getEntityStatus().equals(EntityStatus.ACTIVE)){
            throw new GenericValidationException("pet is already inactive");
        }
        pet.setEntityStatus(EntityStatus.INACTIVE_DELETED);
        log.info("pet is inactive");
    }

    @Override
    public Pet getEntityByUuid(UUID uuid) {
        return petRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("pet not found"));
    }

    @Override
    public List<PetResponseDto> getPetListByOwnerUuid(UUID uuid) {
        return petRepository.findByOwnerUuid(uuid)
                .stream()
                .map(petMapper::mapToDto)
                .toList();
    }
}

