package com.github.farzan6118.petclinic.appointment.service;

import com.github.farzan6118.petclinic.appointment.dto.request.CreateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.appointment.dto.request.UpdateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.appointment.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.appointment.mapper.DurationTemplateMapper;
import com.github.farzan6118.petclinic.appointment.model.DurationTemplate;
import com.github.farzan6118.petclinic.appointment.repository.DurationTemplateRepository;
import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.common.exception.ConflictException;
import com.github.farzan6118.petclinic.common.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.common.mapper.PageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DurationTemplateServiceImpl implements DurationTemplateService {

    private final DurationTemplateRepository durationTemplateRepository;
    private final DurationTemplateMapper durationTemplateMapper;
    private final PageMapper pageMapper;

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
    public PageResponseDto<DurationTemplateResponseDto> findAllPageable(PageAndSortRequestDto requestDto) {
        Pageable pageable = pageMapper.getPageable(requestDto);
        Page<DurationTemplate> durationTemplatePage = durationTemplateRepository
                .findAllByEntityStatus(EntityStatus.ACTIVE, pageable);
        return pageMapper.toPageResponse(durationTemplatePage, durationTemplateMapper::mapToDto);
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
        validateUniqueFields(request.name(), request.durationMinutes());
        DurationTemplate durationTemplate = new DurationTemplate();
        durationTemplateMapper.mapToEntity(request, durationTemplate);
        durationTemplateRepository.save(durationTemplate);
        log.info("Duration template created: {}", durationTemplate.getUuid());
    }

    private void validateUniqueFields(String name, Integer duration) {
        String nameUpperCase = name.trim().toUpperCase(Locale.ROOT);
        if (durationTemplateRepository.existsByName(nameUpperCase)) {
            throw new ConflictException(
                    "durationTemplate exists", "name: " + nameUpperCase + " already exists");
        }

        if (durationTemplateRepository.existsByDurationMinutes(duration)) {
            throw new ConflictException(
                    "durationTemplate exists", "duration " + duration + " already exists");
        }
    }

    @Transactional
    @Override
    public void update(UUID uuid, UpdateDurationTemplateRequestDto request) {
        DurationTemplate durationTemplate = getEntityByUuid(uuid);
        validateNameUniqueness(request.name(), durationTemplate.getUuid());
        validateDurationUniqueness(request.durationMinutes(), durationTemplate.getUuid());
        durationTemplateMapper.mapToEntity(request, durationTemplate);
        log.info("Duration template updated: {}", uuid);
    }

    private void validateNameUniqueness(String name, UUID uuid) {
        String nameUpperCase = name.trim().toUpperCase(Locale.ROOT);
        if (durationTemplateRepository.existsByNameAndUuidNot(nameUpperCase, uuid)) {
            throw new ConflictException("duration.exists", "duration template with name: '" + nameUpperCase + "' already exists");
        }
    }

    private void validateDurationUniqueness(Integer duration, UUID uuid) {
        if (durationTemplateRepository.existsByDurationMinutesAndUuidNot(duration, uuid)) {
            throw new ConflictException("duration.exists", "duration template with duration: '" + duration + "' already exists");
        }
    }

    @Transactional
    @Override
    public void inactivate(UUID uuid) {
        DurationTemplate durationTemplate = getEntityByUuid(uuid);
        if (durationTemplate.getEntityStatus() != EntityStatus.ACTIVE) {
            throw new ConflictException(
                    "duration.template.is.inactive",
                    "Duration template already inactive"
            );
        }
        durationTemplate.setEntityStatus(EntityStatus.INACTIVE);
        log.info("Duration template inactivated: {}", uuid);
    }

    @Transactional
    @Override
    public void activate(UUID uuid) {
        DurationTemplate durationTemplate = durationTemplateRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("duration template not found"));
        if (durationTemplate.getEntityStatus() != EntityStatus.INACTIVE) {
            throw new ConflictException(
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
            throw new ConflictException(
                    "duration.template.is.deleted",
                    "Duration template already deleted");
        }
        entityByUuid.setEntityStatus(EntityStatus.DELETED);
        log.info("duration template deleted");
    }

}
