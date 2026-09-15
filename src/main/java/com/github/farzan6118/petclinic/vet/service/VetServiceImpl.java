package com.github.farzan6118.petclinic.vet.service;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.common.exception.GenericValidationException;
import com.github.farzan6118.petclinic.common.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.common.mapper.PageMapper;
import com.github.farzan6118.petclinic.vet.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.vet.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.vet.dto.request.VetProfileUpdateRequestDto;
import com.github.farzan6118.petclinic.vet.dto.response.VetProfileResponseDto;
import com.github.farzan6118.petclinic.vet.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.vet.mapper.VetMapper;
import com.github.farzan6118.petclinic.vet.model.Vet;
import com.github.farzan6118.petclinic.vet.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;
    private final VetMapper vetMapper;
    private final PageMapper pageMapper;

    @Override
    public VetResponseDto getByUuid(UUID uuid) {
        Vet vet = getEntityByUuid(uuid);
        return vetMapper.mapToDto(vet);
    }

    @Override
    public Vet getEntityByUuid(UUID uuid) {
        return vetRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("vet not found"));
    }

    @Override
    public PageResponseDto<VetResponseDto> findAllPageable(PageAndSortRequestDto requestDto) {
        Pageable pageable = pageMapper.getPageable(requestDto);
        Page<Vet> vetPage = vetRepository.findAll(pageable);
        return pageMapper.toPageResponse(vetPage, vetMapper::mapToDto);
    }

    @Transactional
    @Override
    public void create(CreateVetRequestDto request) {

        validateUniqueContactInfo(request.mobileNumber(), request.email());
        Vet vet = vetMapper.mapToEntity(request);

        vetRepository.save(vet);
        log.info("vet created");
    }

    private void validateUniqueContactInfo(String mobileNumber, String email) {
        if (vetRepository.existsByEmail(email)) {
            throw new GenericValidationException("email exists", "vet email " + email + " exists");
        }
        if (vetRepository.existsByMobileNumber(mobileNumber)) {
            throw new GenericValidationException("mobileNumber exists", "vet mobileNumber " + mobileNumber + " exists");
        }
    }

    @Transactional
    @Override
    public void updateVetProfileByUuid(VetProfileUpdateRequestDto request, UUID vetUuid) {
        Vet vet = getEntityByUuid(vetUuid);
        vet.updateProfile(request.city(), request.address(), request.birthDate(), request.specialty());
        log.info("vet profile updated");
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateVetRequestDto request) {
        Vet vet = getEntityByUuid(uuid);
        validateEmailUniqueness(request.email(), uuid);
        validateTelephoneUniqueness(request.mobileNumber(), uuid);
        vetMapper.mapToEntity(request, vet);
        log.info("vet updated");
    }

    private void validateEmailUniqueness(String email, UUID vetUuid) {
        if (vetRepository.existsByEmailAndUuidNot(email, vetUuid)) {
            throw new GenericValidationException("Vet with this email already exists");
        }
    }

    private void validateTelephoneUniqueness(String mobileNumber, UUID vetUuid) {
        if (vetRepository.existsByMobileNumberAndUuidNot(mobileNumber, vetUuid)) {
            throw new GenericValidationException("Vet with this mobileNumber already exists");
        }
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Vet vet = this.getEntityByUuid(uuid);
        if (!vet.getEntityStatus().equals(EntityStatus.ACTIVE)) {
            throw new GenericValidationException("vet is already inactive");
        }
        vet.setEntityStatus(EntityStatus.INACTIVE_DELETED);
        log.info("vet inactivated");
    }

    @Override
    public VetProfileResponseDto getVetProfileByUuid(UUID uuid) {
        Vet vet = getEntityByUuid(uuid);
        return vetMapper.mapToVetProfileDto(vet);
    }

    @Override
    public Vet getVetWithUuidLock(UUID vetUuid) {
        return vetRepository.findByUuidWithLock(vetUuid).orElseThrow(() -> new ResourceNotFoundException("Vet not found: " + vetUuid));
    }
}

