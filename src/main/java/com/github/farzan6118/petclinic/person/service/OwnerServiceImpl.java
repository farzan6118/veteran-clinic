package com.github.farzan6118.petclinic.person.service;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.common.exception.ConflictException;
import com.github.farzan6118.petclinic.common.exception.NotFoundException;
import com.github.farzan6118.petclinic.common.exception.ValidationException;
import com.github.farzan6118.petclinic.common.mapper.PageMapper;
import com.github.farzan6118.petclinic.owner.model.Owner;
import com.github.farzan6118.petclinic.person.dto.request.CreateOwnerRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.UpdateOwnerRequestDto;
import com.github.farzan6118.petclinic.person.dto.response.OwnerResponseDto;
import com.github.farzan6118.petclinic.person.mapper.OwnerMapper;
import com.github.farzan6118.petclinic.person.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final PageMapper pageMapper;

    @Override
    public OwnerResponseDto getByUuid(UUID uuid) {
        Owner owner = this.getEntityByUuid(uuid);
        return ownerMapper.mapToDto(owner);
    }

    @Override
    public PageResponseDto<OwnerResponseDto> findAll(PageAndSortRequestDto requestDto) {
        Pageable pageable = pageMapper.getPageable(requestDto);
        Page<Owner> ownerPage = ownerRepository.findAll(pageable);
        return pageMapper.toPageResponse(ownerPage, ownerMapper::mapToDto);
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

        if (ownerRepository.existsByPerson_Profile_Email(email)) {
            throw new ConflictException("email exists", "email " + email + " already exists");
        }

        if (ownerRepository.existsByPerson_Profile_MobileNumber(mobile)) {
            throw new ConflictException("mobile exists", "mobile " + mobile + " already exists");
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
        if (ownerRepository.existsByPerson_Profile_EmailAndUuidNot(email, uuid)) {
            throw new NotFoundException("owner with this email already exists");
        }
    }

    private void validateTelephoneUniqueness(String telephone, UUID uuid) {
        if (ownerRepository.existsByPerson_Profile_MobileNumberAndUuidNot(telephone, uuid)) {
            throw new NotFoundException("owner with this mobileNumber already exists");
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Owner birth date cannot be in the future");
        }
    }

    @Transactional
    @Override
    public void inactivate(UUID uuid) {
        Owner owner = getEntityByUuid(uuid);
        if (owner.getEntityStatus() != EntityStatus.ACTIVE) {
            throw new ValidationException(
                    "owner.is.inactive",
                    "owner is already inactive"
            );
        }
        owner.setEntityStatus(EntityStatus.INACTIVE);
        log.info("owner inactivated: {}", uuid);
    }

    @Transactional
    @Override
    public void activate(UUID uuid) {
        Owner owner = ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("owner not found"));
        if (owner.getEntityStatus() != EntityStatus.INACTIVE) {
            throw new ValidationException(
                    "owner.cannot.be.activated",
                    "owner cannot be activated"
            );
        }
        owner.setEntityStatus(EntityStatus.ACTIVE);
        log.info("owner activated: {}", uuid);
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Owner owner = this.getEntityByUuid(uuid);
        if (!owner.getEntityStatus().equals(EntityStatus.ACTIVE)) {
            throw new ValidationException(
                    "owner.is.deleted",
                    "owner is already deleted");
        }
        owner.setEntityStatus(EntityStatus.DELETED);
        log.info("owner has been deleted");
    }

    @Override
    public Owner getEntityByUuid(UUID uuid) {
        return ownerRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("owner not found"));
    }

}

