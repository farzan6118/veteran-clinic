package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreateVetAvailabilityRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetAvailabilityRequestDto;
import com.github.farzan6118.petclinic.dto.response.AvailabilityResponseDto;
import com.github.farzan6118.petclinic.exception.GenericValidationException;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.VetAvailability;
import com.github.farzan6118.petclinic.repository.VetAvailabilityRepository;
import com.github.farzan6118.petclinic.repository.VetRepository;
import com.github.farzan6118.petclinic.service.VetAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        LocalDateTime startDateTime = request.startTime();
        LocalDateTime endDateTime = request.endTime();
        Vet vet = getVet(vetUuid);
        validateTimeRange(startDateTime, endDateTime);
        checkCreateOverlapping(vetUuid, startDateTime, endDateTime);
        VetAvailability availability = new VetAvailability().create(vet, startDateTime, endDateTime);

        availabilityRepository.save(availability);
    }

    private void checkCreateOverlapping(UUID vetUuid, LocalDateTime startTime, LocalDateTime endTime) {
        boolean overlapping = availabilityRepository.existsOverlappingAvailability(vetUuid, startTime, endTime);
        if (overlapping) {
            throw new GenericValidationException("Vet already has an availability overlapping this time range");
        }
    }

    @Override
    @Transactional
    public void updateAvailability(UUID vetUuid, UUID availabilityUuid, UpdateVetAvailabilityRequestDto request) {
        VetAvailability availability = availabilityRepository.findByUuidAndVetUuid(availabilityUuid, vetUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Vet availability not found"));
        LocalDateTime startDateTime = request.startTime();
        LocalDateTime endDateTime = request.endTime();
        validateTimeRange(startDateTime, endDateTime);
        checkUpdateOverlapping(vetUuid, availabilityUuid, startDateTime, endDateTime);
        availability.update(startDateTime, endDateTime);
    }

    private void checkUpdateOverlapping(UUID vetUuid, UUID availabilityUuid,
                                        LocalDateTime startTime, LocalDateTime endTime) {
        boolean overlapping = availabilityRepository.existsOverlappingAvailabilityForUpdate(
                vetUuid, availabilityUuid, startTime, endTime);
        if (overlapping) {
            throw new GenericValidationException("Vet already has an availability overlapping this time range");
        }
    }

    @Override
    @Transactional
    public void deleteAvailability(UUID vetUuid, UUID availabilityUuid) {

        VetAvailability availability = availabilityRepository.findByUuidAndVetUuid(availabilityUuid, vetUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Vet availability not found"));

        availability.setActive(false);
    }

    @Override
    public List<AvailabilityResponseDto> getVetAvailability(UUID vetUuid) {
        return availabilityRepository.findAllByVetUuid(vetUuid)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private AvailabilityResponseDto mapToDto(VetAvailability vetAvailability) {
        return new AvailabilityResponseDto(
                vetAvailability.getUuid(),
                vetAvailability.getStartTime().toLocalDate(),
                vetAvailability.getStartTime().toLocalTime(),
                vetAvailability.getEndTime().toLocalTime(),
                vetAvailability.isActive());
    }

    private Vet getVet(UUID vetUuid) {
        return vetRepository.findByUuid(vetUuid).orElseThrow(() -> new ResourceNotFoundException("Vet not found"));
    }

    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new GenericValidationException("Start time must be before end time");
        }
    }

    @Override
    public Optional<AvailabilityResponseDto> findAvailableByUuidAndTimeRange(
            UUID uuid, LocalDateTime StartTime, LocalDateTime EndTime) {
        vetRepository.findAvailableByUuidAndTimeRange(uuid, StartTime, EndTime);
        return Optional.empty();
    }
}