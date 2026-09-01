package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.exception.ClinicBadRequestException;
import com.github.farzan6118.petclinic.mapper.VetMapper;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.repository.jpa.VetRepository;
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
        Vet vet = getVetByUuid(uuid);
        return vetMapper.mapToDto(vet);
    }

    @Override
    public Vet getVetByUuid(UUID uuid) {
        return vetRepository.findByUuid(uuid)
                .orElseThrow(() -> new ClinicBadRequestException("vet.not.found"));
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
        Vet vet = vetMapper.mapToEntity(request);

        Vet savedVet = vetRepository.save(vet);

        log.info("Vet created successfully. vetId={}", savedVet.getId());

    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateVetRequestDto request) {
        Vet vet = vetRepository.findByUuid(uuid)
                .orElseThrow(() -> new ClinicBadRequestException("vet.not.found"));

        vetMapper.mapToEntity(request, vet);

        log.info("Vet updated successfully. vetUuid={}", uuid);
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Vet vet = vetRepository.findByUuid(uuid)
                .orElseThrow(() -> new ClinicBadRequestException("vet.not.found"));

        vetRepository.delete(vet);

        log.info("Vet deleted successfully. vetUuid={}", uuid);
    }

}

