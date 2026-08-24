package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.controller.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.VetResponseDto;
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
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;

    @Override
    public VetResponseDto getById(UUID uuid) {
        Vet vet = vetRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("vet.not.found"));

        return mapToDto(vet);
    }

    @Override
    public List<VetResponseDto> findAll() {
        return vetRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public VetResponseDto create(CreateVetRequestDto request) {
        Vet vet = new Vet();
        vet.setFirstname(request.firstname());
        vet.setLastname(request.lastname());
        vet.setNationalCode(request.nationalCode());
        vet.setTelephone(request.telephone());
        vet.setEmail(request.email());
        vet.setSpecialty(request.specialty());
        vet.setBirthDate(request.birthDate());
        vet.setAddress(request.address());
        vet.setCity(request.city());

        Vet savedVet = vetRepository.save(vet);

        log.info("Vet created successfully. vetId={}", savedVet.getId());

        return mapToDto(savedVet);
    }

    @Transactional
    @Override
    public VetResponseDto update(UUID uuid, UpdateVetRequestDto request) {
        Vet vet = vetRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("vet.not.found"));

        vet.setFirstname(request.firstname());
        vet.setLastname(request.lastname());
        vet.setNationalCode(request.nationalCode());
        vet.setTelephone(request.telephone());
        vet.setEmail(request.email());
        vet.setSpecialty(request.specialty());
        vet.setBirthDate(request.birthDate());
        vet.setAddress(request.address());
        vet.setCity(request.city());

        log.info("Vet updated successfully. vetUuid={}", uuid);

        return mapToDto(vet);
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Vet vet = vetRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("vet.not.found"));

        vetRepository.delete(vet);

        log.info("Vet deleted successfully. vetUuid={}", uuid);
    }

    private VetResponseDto mapToDto(Vet vet) {
        return new VetResponseDto(
        vet.getUuid(),
        vet.getFirstname(),
        vet.getLastname(),
        vet.getNationalCode(),
        vet.getTelephone(),
        vet.getEmail(),
        vet.getSpecialty(),
        vet.getBirthDate(),
        vet.getAddress(),
        vet.getCity());
    }
}

