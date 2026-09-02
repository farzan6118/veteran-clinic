package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreateOwnerRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateOwnerRequestDto;
import com.github.farzan6118.petclinic.dto.response.OwnerResponseDto;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.model.Owner;
import com.github.farzan6118.petclinic.repository.OwnerRepository;
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
@Transactional(readOnly = true)
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    @Override
    public OwnerResponseDto getByUuid(UUID uuid) {
        Owner owner = ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("owner not found"));
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
    public void create(CreateOwnerRequestDto request) {
        Owner owner = new Owner();
        mapToOwner(request, owner);
        ownerRepository.save(owner);
        log.info("owner created");
    }

    private void mapToOwner(CreateOwnerRequestDto request, Owner owner) {
        owner.setFirstname(request.firstname());
        owner.setLastname(request.lastname());
        owner.setAddress(request.address());
        owner.setNationalCode(request.nationalCode());
        owner.setBirthDate(request.birthDate());
        owner.setCity(request.city());
        owner.setTelephone(request.telephone());
        owner.setEmail(request.email());
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateOwnerRequestDto request) {
        Owner owner = ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("owner not found"));
        mapToOwner(request, owner);
        ownerRepository.save(owner);
        log.info("owner updated");
    }

    private void mapToOwner(UpdateOwnerRequestDto request, Owner owner) {
        owner.setFirstname(request.firstname());
        owner.setLastname(request.lastname());
        owner.setAddress(request.address());
        owner.setNationalCode(request.nationalCode());
        owner.setBirthDate(request.birthDate());
        owner.setCity(request.city());
        owner.setTelephone(request.telephone());
        owner.setEmail(request.email());
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Owner owner = ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("owner not found"));

        ownerRepository.delete(owner);

        log.info("Owner deleted");
    }

    @Override
    public Owner getEntityByUuid(UUID uuid) {
        return ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("owner not found"));
    }

}

