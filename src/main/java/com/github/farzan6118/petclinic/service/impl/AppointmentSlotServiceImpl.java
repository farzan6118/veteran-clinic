package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.exception.GenericValidationException;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.model.AppointmentSlot;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.VetAvailability;
import com.github.farzan6118.petclinic.model.constant.AppointmentDuration;
import com.github.farzan6118.petclinic.model.constant.SlotStatus;
import com.github.farzan6118.petclinic.repository.AppointmentSlotRepository;
import com.github.farzan6118.petclinic.repository.VetAvailabilityRepository;
import com.github.farzan6118.petclinic.repository.VetRepository;
import com.github.farzan6118.petclinic.service.AppointmentSlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private final VetRepository vetRepository;
    private final VetAvailabilityRepository availabilityRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;

    @Transactional
    @Override
    public void generateSlotsForDate(UUID vetUuid, LocalDate date) {

        Vet vet = getVet(vetUuid);

        List<VetAvailability> availabilities =
                availabilityRepository.findAllByVetUuidAndDateAndActiveTrue(vetUuid, date);

        if (availabilities.isEmpty()) {
            log.debug("No active availability found. vetUuid={}, date={}", vetUuid, date);
            return;
        }

        for (VetAvailability availability : availabilities) {

            generateSlots(vet, availability);
        }

        log.info("Appointment slots generated. vetUuid={}, date={}", vetUuid, date);
    }


    @Transactional
    @Override
    public void generateUpcomingSlots(UUID vetUuid, LocalDate from, LocalDate to) {

        if (from.isAfter(to)) {
            throw new GenericValidationException(
                    "appointment.slot.invalid.date.range",
                    "From date must be before or equal to to date"
            );
        }

        getVet(vetUuid);

        LocalDate currentDate = from;

        while (!currentDate.isAfter(to)) {

            generateSlotsForDate(vetUuid, currentDate);

            currentDate = currentDate.plusDays(1);
        }

        log.info("Upcoming appointment slots generated. vetUuid={}, from={}, to={}", vetUuid, from, to);
    }


    private void generateSlots(Vet vet, VetAvailability availability) {
        LocalDate date = availability.getDate();

        LocalTime startTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();

        AppointmentDuration duration = AppointmentDuration.FIFTEEN_MINUTES;

        LocalTime slotStart = startTime;

        while (!slotStart.plusMinutes(duration.getMinutes()).isAfter(endTime)) {

            LocalTime slotEnd = slotStart.plusMinutes(duration.getMinutes());

            AppointmentSlot slot = new AppointmentSlot();
            slot.setVet(vet);
            slot.setDate(date);
            slot.setStartTime(slotStart);
            slot.setEndTime(slotEnd);
            slot.setAppointmentDuration(duration);
            slot.setStatus(SlotStatus.AVAILABLE);

            appointmentSlotRepository.save(slot);

            slotStart = slotEnd;
        }
    }


    private void createSlotIfNotExists(Vet vet, LocalDate date, LocalTime startTime, LocalTime endTime) {

        boolean exists = appointmentSlotRepository.existsByVetUuidAndDateAndStartTime(vet.getUuid(), date, startTime);

        if (exists) {
            return;
        }

        AppointmentSlot slot = new AppointmentSlot();

        slot.setVet(vet);
        slot.setDate(date);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setStatus(SlotStatus.AVAILABLE);

        appointmentSlotRepository.save(slot);
    }


    private Vet getVet(UUID vetUuid) {

        return vetRepository.findByUuid(vetUuid).orElseThrow(
                () -> new ResourceNotFoundException("Vet not found"));
    }


    private void validateAvailability(LocalTime startTime, LocalTime endTime, Integer durationMinutes) {

        if (startTime == null || endTime == null) {
            throw new GenericValidationException(
                    "vet.availability.invalid.time",
                    "Availability time cannot be null"
            );
        }

        if (!startTime.isBefore(endTime)) {
            throw new GenericValidationException(
                    "vet.availability.invalid.time.range",
                    "Start time must be before end time"
            );
        }

        if (durationMinutes == null || durationMinutes <= 0) {
            throw new GenericValidationException(
                    "vet.availability.invalid.duration",
                    "Duration must be greater than zero"
            );
        }

        long availableMinutes = Duration.between(startTime, endTime).toMinutes();

        if (durationMinutes > availableMinutes) {
            throw new GenericValidationException(
                    "vet.availability.invalid.duration",
                    "Appointment duration cannot exceed availability duration"
            );
        }
    }
}
