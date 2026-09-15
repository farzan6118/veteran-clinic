package com.github.farzan6118.petclinic.visit.service;

import com.github.farzan6118.petclinic.common.enums.VisitCategory;
import com.github.farzan6118.petclinic.common.enums.VisitStatus;
import com.github.farzan6118.petclinic.common.exception.GenericValidationException;
import com.github.farzan6118.petclinic.common.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.config.ClinicProperties;
import com.github.farzan6118.petclinic.infrastructure.email.VisitNotificationService;
import com.github.farzan6118.petclinic.pet.model.Pet;
import com.github.farzan6118.petclinic.pet.service.PetService;
import com.github.farzan6118.petclinic.room.model.Room;
import com.github.farzan6118.petclinic.room.service.RoomService;
import com.github.farzan6118.petclinic.vet.model.Vet;
import com.github.farzan6118.petclinic.vet.service.VetAvailabilityService;
import com.github.farzan6118.petclinic.vet.service.VetService;
import com.github.farzan6118.petclinic.visit.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.VisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.visit.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.visit.mapper.VisitMapper;
import com.github.farzan6118.petclinic.visit.model.Visit;
import com.github.farzan6118.petclinic.visit.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitService {

    private final ClinicProperties clinicProperties;
    private final VisitRepository visitRepository;
    private final RoomService roomService;
    private final VisitMapper visitMapper;
    private final PetService petService;
    private final VetService vetService;
    private final VetAvailabilityService vetAvailabilityService;
    private final DurationTemplateService durationTemplateService;
    private final VisitNotificationService visitNotificationService;

    /**
     * Book an available appointment slot for a pet.
     */
    @Override
    @Transactional
    public void bookVisit(VisitRequestDto request) {
        DurationTemplateResponseDto standardDuration = durationTemplateService.findByName("STANDARD");

        LocalDateTime visitStart = LocalDateTime.of(request.visitDate(), request.visitTime());
        LocalDateTime visitEnd = getVisitEnd(visitStart, standardDuration);

        dateAndTimeValidations(visitStart, visitEnd);

        Vet vet = vetService.getVetWithUuidLock(request.vetUuid());
        Pet pet = petService.getEntityByUuid(request.petUuid());
        Room room = roomService.getAvailableRoomByVisitTypeAndVisitCategory(request.visitType(), VisitCategory.ROUTINE);

        Visit visit = new Visit()
                .schedule(vet, pet, room, visitStart, visitEnd, request.visitType(), request.description());

        validateVisitTime(visit);
        vetAvailabilityValidation(visit);
        validateRoomAvailability(visit);
        petAvailabilityValidation(visit);

        Visit savedVisit = visitRepository.save(visit);

        visitNotificationService.notifyBookVisitParticipants(savedVisit, pet, vet);

        log.info("Visit booked successfully. visitUuid={}, petUuid={}, vetUuid={}, roomUuid={}, startTime={}, endTime={}",
                savedVisit.getUuid(),
                pet.getUuid(),
                vet.getUuid(),
                room != null ? room.getUuid() : null, visitStart,
                visitEnd
        );
    }

    private void vetAvailabilityValidation(Visit visit) {
        if (visit.getVet() == null) {
            return;
        }

        String startTime = visit.getStartTime().toString();
        String endTime = visit.getEndTime().toString();

        vetAvailabilityService.findAvailableByUuidAndTimeRange(
                visit.getVet().getUuid(), visit.getStartTime(), visit.getEndTime()).orElseThrow(() -> {
            String message = String.format("the vet is not available from %s to %s", startTime, endTime);
            return new GenericValidationException(message, message);
        });

        boolean existsVetReservation = visitRepository.existsVetReservation(
                visit.getVet().getUuid(), visit.getStartTime(), visit.getEndTime(), null);

        if (existsVetReservation) {
            String message = String.format("the vet is already booked from %s to %s", startTime, endTime);
            throw new GenericValidationException(message, message);
        }
    }

    private void validateVisitTime(Visit visit) {
        LocalDate visitDate = visit.getStartTime().toLocalDate();

        if (clinicProperties.closeDays().contains(visitDate.getDayOfWeek())) {
            String message = String.format("the clinic is closed on %s (%s)", visitDate.getDayOfWeek(), visitDate);
            throw new GenericValidationException(message, message);
        }

        ClinicProperties.WorkingHours workingHours = clinicProperties.workingHours();

        if (visit.getStartTime().toLocalTime().isBefore(workingHours.start()) ||
                visit.getEndTime().toLocalTime().isAfter(workingHours.end())) {

            String message = String.format("the visit must be scheduled between %s and %s on %s",
                    workingHours.start(), workingHours.end(), visitDate);

            throw new GenericValidationException(message, message);
        }
    }

    private void validateRoomAvailability(Visit visit) {
        if (visit.getRoom() == null) {
            return;
        }

        boolean existsRoomReservation = visitRepository.existsRoomReservation(
                visit.getRoom().getUuid(), visit.getStartTime(), visit.getEndTime(), null);

        if (existsRoomReservation) {
            String message = String.format("the room %s is already booked from %s to %s",
                    visit.getRoom().getName(), visit.getStartTime(), visit.getEndTime());

            throw new GenericValidationException(message, message);
        }
    }

    private void petAvailabilityValidation(Visit visit) {
        boolean existsPetReservation = visitRepository.existsPetReservation(
                visit.getPet().getUuid(), visit.getStartTime(), visit.getEndTime(), null);

        if (existsPetReservation) {
            String message = String.format("the pet already has an appointment from %s to %s",
                    visit.getStartTime(), visit.getEndTime());

            throw new GenericValidationException(message, message);
        }
    }

    private void dateAndTimeValidations(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new GenericValidationException(
                    "visit end time must be after start time",
                    "visit end time must be after start time");
        }

        if (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            throw new GenericValidationException(
                    "visit start and end time must be on the same day",
                    "visit start and end time must be on the same day");
        }
    }

    @Override
    public VisitResponseDto getByUuid(UUID uuid) {

        Visit visit = getVisitByUuid(uuid);

        return visitMapper.toResponse(visit);
    }

    @Override
    @Transactional
    public void cancelVisit(UUID uuid, String reason) {

        Visit visit = getVisitByUuid(uuid);

        if (visit.getStatus() == VisitStatus.CANCELLED) {
            return;
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new ResourceNotFoundException("visit.already.completed", "Completed visit cannot be cancelled");
        }

        visit.cancel();

        visitNotificationService.notifyCancelVisitParticipants(visit, visit.getPet(), visit.getVet(), reason);

        log.info("Visit cancelled. visitUuid={}", visit.getUuid());
    }

    @Override
    public List<VisitResponseDto> findAllVisitsByVetUuid(UUID vetUuid, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return visitRepository.findAllVisitsByVetUuidAndStartTimeBetween(vetUuid, startOfDay, endOfDay)
                .stream().map(visitMapper::toResponse).toList();
    }

    @Override
    public List<VisitResponseDto> findAllVisitsByPetUuid(UUID petUuid, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return visitRepository.findAllVisitsByPetUuidAndStartTimeBetween(petUuid, startOfDay, endOfDay)
                .stream().map(visitMapper::toResponse).toList();
    }

    @Override
    public List<VisitResponseDto> findAllVisitsByRoomUuid(UUID RoomUuid, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return visitRepository.findAllVisitsByPetUuidAndStartTimeBetween(RoomUuid, startOfDay, endOfDay)
                .stream().map(visitMapper::toResponse).toList();
    }

    /**
     * Complete a visit.
     */
    @Override
    @Transactional
    public void completeVisit(UUID uuid, CompleteVisitRequestDto request) {
        Visit visit = getVisitForUpdate(uuid);
        validateCompletion(visit);
        visit.complete(LocalDateTime.now());
        log.info("Visit completed successfully. visitUuid={}", uuid);
    }

    private Visit getVisitForUpdate(UUID uuid) {
        return visitRepository.findByUuidForUpdate(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("visit.not.found", "Visit not found: " + uuid));
    }

    private void validateCompletion(Visit visit) {
        validateStatus(visit);
        validateScheduledTime(visit);
    }

    private void validateStatus(Visit visit) {

        switch (visit.getStatus()) {

            case CANCELLED ->
                    throw new GenericValidationException("visit.cancelled", "Cancelled visit cannot be completed");

            case COMPLETED ->
                    throw new GenericValidationException("visit.already.completed", "Visit is already completed");

            default -> {
                // Valid states can continue.
            }
        }
    }

    private void validateScheduledTime(Visit visit) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startTime = visit.getStartTime();
        LocalDateTime endTime = visit.getEndTime();

        if (now.isBefore(startTime)) {
            throw new GenericValidationException("visit.not.started", "Visit has not started yet");
        }

        if (now.isAfter(endTime)) {
            throw new GenericValidationException("visit.already.finished", "Visit has already finished");
        }
    }

    /**
     * Get all visits.
     */
    @Override
    public List<VisitResponseDto> getAllVisits() {
        return visitRepository.findAll().stream().map(visitMapper::toResponse).toList();
    }

    /**
     * Reschedule a visit to another available slot.
     */
    @Override
    @Transactional
    public void rescheduleVisit(UUID uuid, RescheduleVisitRequestDto request) {
        Visit visit = visitRepository.findByUuidForUpdate(uuid).orElseThrow(
                () -> new ResourceNotFoundException("visit.not.found", "Visit not found: " + uuid));
        validateCanBeRescheduled(visit);

        DurationTemplateResponseDto standardDuration = durationTemplateService.findByName("STANDARD");
        LocalDateTime newVisitStart = LocalDateTime.of(request.date(), request.startTime());
        LocalDateTime newVisitEnd = getVisitEnd(newVisitStart, standardDuration);

        Vet vet = vetService.getVetWithUuidLock(visit.getVet().getUuid());

        LocalDateTime oldVisitStart = visit.getStartTime();

        Room newRoom = roomService.getAvailableRoomByVisitTypeAndVisitCategory(visit.getVisitType(), VisitCategory.ROUTINE);

        visit.reschedule(newRoom, newVisitStart, newVisitEnd, visit.getVisitType(), request.description());

        visitNotificationService.notifyRescheduleVisitParticipants(visit, visit.getPet(), vet, oldVisitStart);

        log.info("Visit rescheduled successfully. visitUuid={}, oldVisitStart={}, newVisitStart={}",
                visit.getUuid(), oldVisitStart, newVisitStart);
    }

    private Visit getVisitByUuid(UUID uuid) {
        return visitRepository.findByUuid(uuid).orElseThrow(
                () -> new ResourceNotFoundException("Visit not found: " + uuid));
    }

    private LocalDateTime getVisitEnd(LocalDateTime visitStart, DurationTemplateResponseDto duration) {
        return visitStart.plusMinutes(duration.durationMinutes());
    }

    private void validateCanBeRescheduled(Visit visit) {
        if (visit.getStatus() == VisitStatus.CANCELLED) {
            throw new ResourceNotFoundException("visit.cancelled", "Cancelled visit cannot be rescheduled");
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new ResourceNotFoundException("visit.already.completed", "Completed visit cannot be rescheduled");
        }
    }

    private UUID getCurrentUserUuid(Jwt jwt) {
        return UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
    }

}