package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CreateWeeklyAvailabilityRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateWeeklyAvailabilityRequestDto;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.VetAvailability;
import com.github.farzan6118.petclinic.repository.VetAvailabilityRepository;
import com.github.farzan6118.petclinic.repository.VetRepository;
import com.github.farzan6118.petclinic.service.VetScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VetScheduleServiceImpl implements VetScheduleService {

    private final VetRepository vetRepository;
    private final VetAvailabilityRepository vetAvailabilityRepository;

    @Transactional
    @Override
    public void createWeeklyAvailability(UUID vetUuid, CreateWeeklyAvailabilityRequestDto request) {

        getVet(vetUuid);

        validateTimeRange(request.availableFrom(), request.availableTo());

        validateDuration(request.availableFrom(), request.availableTo(), request.durationMinutes());

        validateNoOverlap(
                vetUuid,
                request.dayOfWeek(),
                request.availableFrom(),
                request.availableTo()
        );

        VetAvailability availability = new VetAvailability();

        availability.setVet(getVet(vetUuid));
        availability.setDayOfWeek(request.dayOfWeek());
        availability.setStartTime(request.availableFrom());
        availability.setEndTime(request.availableTo());
        availability.setDurationMinutes(request.durationMinutes());
        availability.setActive(true);

        vetAvailabilityRepository.save(availability);

        log.info("Weekly availability created. vetUuid={}, day={}, from={}, to={}",
                vetUuid, request.dayOfWeek(), request.availableFrom(), request.availableTo());
    }

    @Transactional
    @Override
    public void updateWeeklyAvailability(UUID vetUuid, Long availabilityId, UpdateWeeklyAvailabilityRequestDto request) {

        VetAvailability availability = getAvailability(vetUuid, availabilityId);

        validateTimeRange(request.availableFrom(), request.availableTo());

        validateDuration(request.availableFrom(), request.availableTo(), request.durationMinutes());

        validateNoOverlap(
                vetUuid,
                availabilityId,
                request.dayOfWeek(),
                request.availableFrom(),
                request.availableTo()
        );

        availability.updateSchedule(
                request.dayOfWeek(),
                request.availableFrom(),
                request.availableTo(),
                request.durationMinutes()
        );

        log.info("Weekly availability updated. vetUuid={}, availabilityId={}", vetUuid, availabilityId);
    }

    @Transactional
    @Override
    public void deleteWeeklyAvailability(UUID vetUuid, Long availabilityId) {

        VetAvailability availability = getAvailability(vetUuid, availabilityId);

        vetAvailabilityRepository.delete(availability);

        log.info("Weekly availability deleted. vetUuid={}, availabilityId={}", vetUuid, availabilityId);
    }

    private Vet getVet(UUID vetUuid) {

        return vetRepository.findByUuid(vetUuid)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vet not found"
                        )
                );
    }

    private VetAvailability getAvailability(UUID vetUuid, Long availabilityId) {

        return vetAvailabilityRepository.findByIdAndVetUuid(availabilityId, vetUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Vet availability not found"));
    }


    private void validateTimeRange(LocalTime from, LocalTime to) {

        if (!from.isBefore(to)) {
            throw new ResourceNotFoundException("Available from must be before available to");
        }
    }

    private void validateDuration(LocalTime from, LocalTime to, Integer durationMinutes) {

        long totalMinutes = Duration.between(from, to).toMinutes();

        if (durationMinutes > totalMinutes) {
            throw new ResourceNotFoundException("Appointment duration cannot exceed availability range");
        }
    }

    private void validateNoOverlap(UUID vetUuid, DayOfWeek dayOfWeek, LocalTime from, LocalTime to) {

        if (vetAvailabilityRepository.existsOverlappingAvailability(vetUuid, dayOfWeek, from, to)) {
            throw new ResourceNotFoundException("Vet availability overlaps with an existing availability");
        }
    }

    private void validateNoOverlap(
            UUID vetUuid,
            Long availabilityId,
            DayOfWeek dayOfWeek,
            LocalTime from,
            LocalTime to
    ) {

        if (vetAvailabilityRepository.existsOverlappingAvailabilityForUpdate(
                vetUuid,
                availabilityId,
                dayOfWeek,
                from,
                to
        )) {

            throw new ResourceNotFoundException("Vet availability overlaps with an existing availability");
        }
    }
}