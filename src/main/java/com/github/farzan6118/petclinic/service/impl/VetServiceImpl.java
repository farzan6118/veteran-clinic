package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.VetProfileUpdateRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetProfileResponseDto;
import com.github.farzan6118.petclinic.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.exception.EmailAlreadyExistsException;
import com.github.farzan6118.petclinic.exception.PhoneAlreadyExistsException;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.mapper.VetMapper;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.repository.VetRepository;
import com.github.farzan6118.petclinic.service.VetService;
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
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;
    private final VetMapper vetMapper;

    @Override
    public VetResponseDto getByUuid(UUID uuid) {
        Vet vet = getEntityByUuid(uuid);
        return vetMapper.mapToDto(vet);
    }

    @Override
    public Vet getEntityByUuid(UUID uuid) {
        return vetRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("vet.not.found"));
    }

    @Override
    public List<VetResponseDto> findAll() {
        return vetRepository.findAll()
                .stream()
                .map(vetMapper::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public void create(CreateVetRequestDto request) {
        validateUniqueContactInfo(request.telephone(), request.email());
        vetMapper.mapToEntity(request);
        log.info("Vet created successfully.");

    }

    private void validateUniqueContactInfo(String telephone, String email) {

        if (vetRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("vet with email " + email + " already exists");
        }

        if (vetRepository.existsByTelephone(telephone)) {
            throw new PhoneAlreadyExistsException("vet with telephone " + telephone + " already exists");
        }
    }

    @Transactional
    @Override
    public void updateVetProfileByUuid(VetProfileUpdateRequestDto request, UUID vetUuid) {
        Vet vet = getEntityByUuid(vetUuid);
        vet.updateProfile(request.city(), request.address(), request.birthDate(), request.specialty());
        log.info("Vet profile updated successfully. vetUuid={}", vetUuid);
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateVetRequestDto request) {

        Vet vet = getEntityByUuid(uuid);

        validateEmailUniqueness(request.email(), uuid);

        validateTelephoneUniqueness(request.telephone(), uuid);

        vetMapper.mapToEntity(request, vet);

        log.info("Vet updated successfully. vetUuid={}", uuid);
    }

    private void validateEmailUniqueness(String email, UUID vetUuid) {
        if (vetRepository.existsByEmailAndUuidNot(email, vetUuid)) {
            throw new ResourceNotFoundException("Vet with this email already exists");
        }
    }

    private void validateTelephoneUniqueness(String telephone, UUID vetUuid) {
        if (vetRepository.existsByTelephoneAndUuidNot(telephone, vetUuid)) {
            throw new ResourceNotFoundException("Vet with this telephone already exists");
        }
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Vet vet = vetRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("vet not found"));

        vetRepository.delete(vet);

        log.info("Vet deleted successfully. vetUuid={}", uuid);
    }

    @Override
    public VetProfileResponseDto getVetProfileByUuid(UUID uuid) {
        Vet vet = getEntityByUuid(uuid);
        return vetMapper.mapToVetProfileDto(vet);
    }
}

