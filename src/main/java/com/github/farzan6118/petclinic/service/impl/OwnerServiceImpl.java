package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreateOwnerRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateOwnerRequestDto;
import com.github.farzan6118.petclinic.dto.response.OwnerResponseDto;
import com.github.farzan6118.petclinic.exception.EmailAlreadyExistsException;
import com.github.farzan6118.petclinic.exception.GenericValidationException;
import com.github.farzan6118.petclinic.exception.PhoneAlreadyExistsException;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.mapper.OwnerMapper;
import com.github.farzan6118.petclinic.model.Owner;
import com.github.farzan6118.petclinic.model.constant.EntityStatus;
import com.github.farzan6118.petclinic.repository.OwnerRepository;
import com.github.farzan6118.petclinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Override
    public OwnerResponseDto getByUuid(UUID uuid) {
        Owner owner = this.getEntityByUuid(uuid);
        return ownerMapper.mapToDto(owner);
    }

    @Override
    public List<OwnerResponseDto> findAll() {
        return ownerRepository.findAll()
                .stream()
                .map(ownerMapper::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public void create(CreateOwnerRequestDto request) {
        Owner owner = new Owner();
        validateBirthDate(request.birthDate());
        validateUniqueContactInfo(request.mobileNumber(), request.email());
        ownerMapper.mapToOwner(request, owner);
        ownerRepository.save(owner);
        log.info("owner created");
    }

    private void validateUniqueContactInfo(String mobile, String email) {

        if (ownerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("email exists", "email " + email + " already exists");
        }

        if (ownerRepository.existsByMobileNumber(mobile)) {
            throw new PhoneAlreadyExistsException("mobile exists", "mobile " + mobile + " already exists");
        }
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateOwnerRequestDto request) {
        validateBirthDate(request.birthDate());
        validateEmailUniqueness(request.email(), uuid);
        validateTelephoneUniqueness(request.mobileNumber(), uuid);
        Owner owner = this.getEntityByUuid(uuid);
        ownerMapper.mapToOwner(request, owner);
        ownerRepository.save(owner);
        log.info("owner updated");
    }

    private void validateEmailUniqueness(String email, UUID uuid) {
        if (ownerRepository.existsByEmailAndUuidNot(email, uuid)) {
            throw new ResourceNotFoundException("owner with this email already exists");
        }
    }

    private void validateTelephoneUniqueness(String telephone, UUID uuid) {
        if (ownerRepository.existsByMobileNumberAndUuidNot(telephone, uuid)) {
            throw new ResourceNotFoundException("owner with this mobileNumber already exists");
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new GenericValidationException("Owner birth date cannot be in the future");
        }
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Owner owner = this.getEntityByUuid(uuid);
        if (!owner.getEntityStatus().equals(EntityStatus.ACTIVE)) {
            throw new GenericValidationException("owner is already inactive");
        }
        owner.setEntityStatus(EntityStatus.INACTIVE_DELETED);
        log.info("Owner deleted");
    }

    @Override
    public Owner getEntityByUuid(UUID uuid) {
        return ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("owner not found"));
    }

}

