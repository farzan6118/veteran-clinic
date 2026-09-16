package com.github.farzan6118.petclinic.pet.service;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.common.exception.NotFoundException;
import com.github.farzan6118.petclinic.common.exception.ValidationException;
import com.github.farzan6118.petclinic.common.mapper.PageMapper;
import com.github.farzan6118.petclinic.owner.model.Owner;
import com.github.farzan6118.petclinic.owner.service.OwnerService;
import com.github.farzan6118.petclinic.pet.dto.request.CreatePetRequestDto;
import com.github.farzan6118.petclinic.pet.dto.request.UpdatePetRequestDto;
import com.github.farzan6118.petclinic.pet.dto.response.PetResponseDto;
import com.github.farzan6118.petclinic.pet.mapper.PetMapper;
import com.github.farzan6118.petclinic.pet.model.Pet;
import com.github.farzan6118.petclinic.pet.model.Species;
import com.github.farzan6118.petclinic.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final PageMapper pageMapper;

    @Override
    public PetResponseDto getByUuid(UUID uuid) {
        Pet pet = this.getEntityByUuid(uuid);
        return petMapper.mapToDto(pet);
    }


    @Override
    public PageResponseDto<PetResponseDto> findAll(PageAndSortRequestDto requestDto) {
        Pageable pageable = pageMapper.getPageable(requestDto);
        Page<Pet> petPage = petRepository.findAll(pageable);
        return pageMapper.toPageResponse(petPage, petMapper::mapToDto);
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
            throw new ValidationException("owner's pet already exists");
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
            throw new ValidationException("owner's pet already exists");
        }
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Pet pet = this.getEntityByUuid(uuid);
        if (!pet.getEntityStatus().equals(EntityStatus.ACTIVE)) {
            throw new ValidationException("pet is already inactive");
        }
        pet.setEntityStatus(EntityStatus.INACTIVE_DELETED);
        log.info("pet is inactive");
    }

    @Override
    public Pet getEntityByUuid(UUID uuid) {
        return petRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("pet not found"));
    }

    @Override
    public List<PetResponseDto> getPetListByOwnerUuid(UUID uuid) {
        return petRepository.findByOwnerUuid(uuid)
                .stream()
                .map(petMapper::mapToDto)
                .toList();
    }
}

