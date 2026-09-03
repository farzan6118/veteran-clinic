package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreateVetAvailabilityRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetAvailabilityRequestDto;
import com.github.farzan6118.petclinic.exception.DateTimeValidationException;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.VetAvailability;
import com.github.farzan6118.petclinic.repository.VetAvailabilityRepository;
import com.github.farzan6118.petclinic.repository.VetRepository;
import com.github.farzan6118.petclinic.service.VetAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VetAvailabilityServiceImpl implements VetAvailabilityService {

    private final VetAvailabilityRepository availabilityRepository;
    private final VetRepository vetRepository;

    @Override
    @Transactional
    public void createAvailability(UUID vetUuid, CreateVetAvailabilityRequestDto request) {
        Vet vet = getVet(vetUuid);

        validateTimeRange(request.startTime(), request.endTime());

        boolean overlapping = availabilityRepository.existsOverlappingAvailability(
                vetUuid, request.date(), request.startTime(), request.endTime());

        if (overlapping) {
            throw new DateTimeValidationException("Vet already has an availability overlapping this time range");
        }

        VetAvailability availability = VetAvailability.create(
                vet, request.date(), request.startTime(), request.endTime());

        availabilityRepository.save(availability);
    }

    @Override
    @Transactional
    public void updateAvailability(UUID vetUuid, UUID availabilityUuid, UpdateVetAvailabilityRequestDto request) {
        VetAvailability availability = availabilityRepository.findByUuidAndVetUuid(availabilityUuid, vetUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Vet availability not found"));

        validateTimeRange(request.startTime(), request.endTime());

        boolean overlapping = availabilityRepository.existsOverlappingAvailabilityForUpdate(
                vetUuid, availabilityUuid, request.date(), request.startTime(), request.endTime());

        if (overlapping) {
            throw new DateTimeValidationException("Vet already has an availability overlapping this time range");
        }

        availability.updateSchedule(request.date(), request.startTime(), request.endTime());
    }

    @Override
    @Transactional
    public void deleteAvailability(UUID vetUuid, UUID availabilityUuid) {

        VetAvailability availability = availabilityRepository.findByUuidAndVetUuid(availabilityUuid, vetUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Vet availability not found"));

        availability.setActive(false);
    }

    private Vet getVet(UUID vetUuid) {
        return vetRepository.findByUuid(vetUuid).orElseThrow(() -> new ResourceNotFoundException("Vet not found"));
    }

    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new DateTimeValidationException("Start time must be before end time");
        }
    }
}