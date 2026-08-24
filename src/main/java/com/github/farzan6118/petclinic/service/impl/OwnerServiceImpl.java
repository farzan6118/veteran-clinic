package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.controller.dto.request.CreateOwnerRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdateOwnerRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.OwnerResponseDto;
import com.github.farzan6118.petclinic.model.Owner;
import com.github.farzan6118.petclinic.repository.jpa.OwnerRepository;
import com.github.farzan6118.petclinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    @Override
    public OwnerResponseDto getById(UUID uuid) {
        Owner owner = ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("owner.not.found"));
        return mapToDto(owner);
    }

    private OwnerResponseDto mapToDto(Owner owner) {
        return new OwnerResponseDto(
                owner.getUuid(),
                owner.getFirstname(),
                owner.getLastname(),
                owner.getNationalCode(),
                owner.getBirthDate(),
                owner.getTelephone(),
                owner.getEmail(),
                owner.getCity(),
                owner.getAddress()
        );
    }

    @Override
    public List<OwnerResponseDto> findAll() {
        return ownerRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public OwnerResponseDto create(CreateOwnerRequestDto request) {
        Owner owner = new Owner();
        owner.setFirstname(request.firstname());
        owner.setLastname(request.lastname());
        owner.setAddress(request.address());
        owner.setNationalCode(request.nationalCode());
        owner.setBirthDate(request.birthDate());
        owner.setCity(request.city());
        owner.setTelephone(request.telephone());
        owner.setEmail(request.email());

        Owner savedOwner = ownerRepository.save(owner);

        log.info("Owner created successfully. ownerId={}", savedOwner.getId());

        return mapToDto(owner);
    }

    @Transactional
    @Override
    public OwnerResponseDto update(UUID uuid, UpdateOwnerRequestDto request) {
        Owner owner = ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("owner.not.found"));

        owner.setFirstname(request.firstname());
        owner.setLastname(request.lastname());
        owner.setAddress(request.address());
        owner.setEmail(request.email());
        owner.setCity(request.city());
        owner.setBirthDate(request.birthDate());
        owner.setTelephone(request.telephone());
        owner.setNationalCode(request.nationalCode());

        log.info("Owner updated successfully. ownerUuid={}", uuid);

        return mapToDto(owner);
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Owner owner = ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("owner.not.found"));

        ownerRepository.delete(owner);

        log.info("Owner deleted successfully. ownerUuid={}", uuid);
    }
}

