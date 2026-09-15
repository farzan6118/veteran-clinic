package com.github.farzan6118.petclinic.pet.service;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.common.exception.GenericValidationException;
import com.github.farzan6118.petclinic.common.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.common.mapper.PageMapper;
import com.github.farzan6118.petclinic.pet.dto.request.CreateSpeciesRequestDto;
import com.github.farzan6118.petclinic.pet.dto.request.UpdateSpeciesRequestDto;
import com.github.farzan6118.petclinic.pet.dto.response.SpeciesResponseDto;
import com.github.farzan6118.petclinic.pet.mapper.SpeciesMapper;
import com.github.farzan6118.petclinic.pet.model.Species;
import com.github.farzan6118.petclinic.pet.repository.SpeciesRepository;
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
public class SpeciesServiceImpl implements SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final SpeciesMapper speciesMapper;
    private final PageMapper pageMapper;

    @Override
    public SpeciesResponseDto getByUuid(UUID uuid) {
        Species species = getEntityByUuid(uuid);
        return speciesMapper.mapToDto(species);
    }

    @Override
    public Species getEntityByUuid(UUID uuid) {
        return speciesRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("species not found"));
    }

    @Override
    public PageResponseDto<SpeciesResponseDto> findAll(PageAndSortRequestDto requestDto) {
        Pageable pageable = pageMapper.getPageable(requestDto);
        Page<Species> speciesPaged = speciesRepository.findAll(pageable);
        return pageMapper.toPageResponse(speciesPaged, speciesMapper::mapToDto);
    }

    @Transactional
    @Override
    public void create(CreateSpeciesRequestDto request) {
        validateUniqueContactInfo(request.code());
        Species species = new Species();
        speciesMapper.mapToSpecies(request, species);
        speciesRepository.save(species);
        log.info("species created");
    }

    private void validateUniqueContactInfo(String code) {
        if (speciesRepository.existsByCode(code)) {
            throw new GenericValidationException("species exists",
                    "species with code: '" + code + "' already exists");
        }
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateSpeciesRequestDto request) {
        Species species = this.getEntityByUuid(uuid);
        validateEmailUniqueness(species.getCode(), uuid);
        speciesMapper.mapToSpecies(request, species);
        log.info("species updated");
    }

    private void validateEmailUniqueness(String code, UUID uuid) {
        if (speciesRepository.existsByCodeAndUuidNot(code, uuid)) {
            throw new ResourceNotFoundException("species with this code already exists");
        }
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        Species species = this.getEntityByUuid(uuid);
        species.setEntityStatus(EntityStatus.INACTIVE_DELETED);
        log.info("species is inactive deleted");
    }
}