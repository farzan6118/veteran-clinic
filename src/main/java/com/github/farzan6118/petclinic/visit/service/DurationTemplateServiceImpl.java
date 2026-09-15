package com.github.farzan6118.petclinic.visit.service;

import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.common.exception.GenericValidationException;
import com.github.farzan6118.petclinic.common.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.visit.dto.request.CreateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.UpdateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.visit.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.visit.mapper.DurationTemplateMapper;
import com.github.farzan6118.petclinic.visit.model.DurationTemplate;
import com.github.farzan6118.petclinic.visit.repository.DurationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DurationTemplateServiceImpl implements DurationTemplateService {


    private final DurationTemplateRepository durationTemplateRepository;
    private final DurationTemplateMapper durationTemplateMapper;

    @Override
    public DurationTemplateResponseDto getByUuid(UUID uuid) {
        DurationTemplate durationTemplate = getEntityByUuid(uuid);
        return durationTemplateMapper.mapToDto(durationTemplate);
    }

    @Override
    public DurationTemplate getEntityByUuid(UUID uuid) {
        return durationTemplateRepository.findByUuidAndEntityStatus(uuid, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("duration template not found"));
    }

    @Override
    public List<DurationTemplateResponseDto> findAll() {
        return durationTemplateRepository.findAllByEntityStatus(EntityStatus.ACTIVE)
                .stream()
                .map(durationTemplateMapper::mapToDto)
                .toList();
    }

    @Override
    public DurationTemplateResponseDto findByName(String name) {
        DurationTemplate durationTemplate = durationTemplateRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("duration template not found"));
        return durationTemplateMapper.mapToDto(durationTemplate);
    }

    @Override
    public DurationTemplateResponseDto findByDuration(Integer duration) {
        DurationTemplate durationTemplate = durationTemplateRepository.findByDurationMinutes(duration)
                .orElseThrow(() -> new ResourceNotFoundException("duration template not found"));
        return durationTemplateMapper.mapToDto(durationTemplate);
    }

    @Transactional
    @Override
    public void create(CreateDurationTemplateRequestDto request) {
        validateUniqueContactInfo(request.name(), request.durationMinutes());
        DurationTemplate durationTemplate = new DurationTemplate();
        durationTemplateMapper.mapToEntity(request, durationTemplate);
        durationTemplateRepository.save(durationTemplate);
        log.info("Duration template created: {}", durationTemplate.getUuid());
    }

    private void validateUniqueContactInfo(String name, Integer duration) {
        String nameUpperCase = name.toUpperCase(Locale.ROOT);
        if (durationTemplateRepository.existsByName(nameUpperCase)) {
            throw new GenericValidationException(
                    "durationTemplate exists", "name: " + nameUpperCase + " already exists");
        }

        if (durationTemplateRepository.existsByDurationMinutes(duration)) {
            throw new GenericValidationException(
                    "durationTemplate exists", "duration " + duration + " already exists");
        }
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateDurationTemplateRequestDto request) {
        DurationTemplate durationTemplate = getEntityByUuid(uuid);
        validateNameUniqueness(durationTemplate.getName(), durationTemplate.getUuid());
        validateDurationUniqueness(durationTemplate.getDurationMinutes(), durationTemplate.getUuid());
        durationTemplateMapper.mapToEntity(request, durationTemplate);
        log.info("Duration template updated: {}", uuid);
    }

    private void validateNameUniqueness(String name, UUID uuid) {
        String nameUpperCase = name.toUpperCase(Locale.ROOT);
        if (durationTemplateRepository.existsByNameAndUuidNot(nameUpperCase, uuid)) {
            throw new GenericValidationException("duration.exists", "duration template with name: '" + nameUpperCase + "' already exists");
        }
    }

    private void validateDurationUniqueness(Integer duration, UUID uuid) {
        if (durationTemplateRepository.existsByDurationMinutesAndUuidNot(duration, uuid)) {
            throw new GenericValidationException("duration.exists", "duration template with duration: '" + duration + "' already exists");
        }
    }

    @Transactional
    @Override
    public void inactivate(UUID uuid) {
        DurationTemplate durationTemplate = getEntityByUuid(uuid);
        if (durationTemplate.getEntityStatus() != EntityStatus.ACTIVE) {
            throw new GenericValidationException(
                    "duration.template.is.inactive",
                    "Duration template already inactive"
            );
        }
        durationTemplate.setEntityStatus(EntityStatus.INACTIVE_NOT_DELETED);
        log.info("Duration template inactivated: {}", uuid);
    }

    @Transactional
    @Override
    public void activate(UUID uuid) {
        DurationTemplate durationTemplate = durationTemplateRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("duration template not found"));
        if (durationTemplate.getEntityStatus() != EntityStatus.INACTIVE_NOT_DELETED) {
            throw new GenericValidationException(
                    "duration.template.cannot.be.activated",
                    "Duration template cannot be activated"
            );
        }
        durationTemplate.setEntityStatus(EntityStatus.ACTIVE);
        log.info("Duration template activated: {}", uuid);
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        DurationTemplate entityByUuid = this.getEntityByUuid(uuid);
        if (!entityByUuid.getEntityStatus().equals(EntityStatus.ACTIVE)) {
            throw new GenericValidationException(
                    "duration.template.is.deleted",
                    "Duration template already deleted");
        }
        entityByUuid.setEntityStatus(EntityStatus.INACTIVE_DELETED);
        log.info("duration template deleted");
    }

}