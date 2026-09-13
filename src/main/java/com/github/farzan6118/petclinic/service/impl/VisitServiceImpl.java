package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.dto.request.VisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.dto.response.VetAvailableSlotResponseDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.exception.GenericValidationException;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.mapper.VisitMapper;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Room;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.Visit;
import com.github.farzan6118.petclinic.model.constant.SlotStatus;
import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.model.constant.VisitType;
import com.github.farzan6118.petclinic.repository.*;
import com.github.farzan6118.petclinic.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

    private final VisitNotificationService visitNotificationService;
    private final VisitRepository visitRepository;
    private final AppointmentSlotRepository slotRepository;
    private final PetService petService;
    private final VisitMapper visitMapper;
    private final VetRepository vetRepository;
    private final VetAvailabilityRepository availabilityRepository;
    private final RoomRepository roomRepository;
    private final AppointmentSlotService appointmentSlotService;
    private final DurationTemplateService durationTemplateService;

    /**
     * Book an available appointment slot for a pet.
     */
    @Override
    @Transactional
    public void bookVisit(VisitRequestDto request) {
        // todo: later work on the business
        DurationTemplateResponseDto standardDuration = durationTemplateService.findByName("STANDARD");
        LocalDateTime visitStart = LocalDateTime.of(request.visitDate(), request.visitTime());
        LocalDateTime visitEnd = getVisitEnd(visitStart, standardDuration);
        dateAndTimeValidations(visitStart, visitEnd);
        Pet pet = petService.getEntityByUuid(request.petUuid());
        Vet vet = getVetWithUuidWithLock(request.vetUuid());
        Room room = reserveResources(vet, request.visitType(), visitStart, visitEnd, null);
        Visit visit = new Visit()
                .schedule(vet, pet, room, visitStart, visitEnd, request.visitType(), request.description());
        Visit savedVisit = visitRepository.save(visit);
        visitNotificationService.notifyBookVisitParticipants(savedVisit, pet, vet);
        log.info("Visit booked successfully. visitUuid={}, petUuid={}, vetUuid={}",
                savedVisit.getUuid(), pet.getUuid(), vet.getUuid());
    }

    private void dateAndTimeValidations(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        if (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            throw new IllegalArgumentException("the start and end time must be the same day");
        }
    }

    @Override
    public List<VisitResponseDto> getMyVisits(Jwt jwt) {

        UUID currentUserUuid = getCurrentUserUuid(jwt);

        return visitRepository.findAllByPetOwnerUuid(currentUserUuid)
                .stream()
                .map(visitMapper::toResponse)
                .toList();
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
    public List<VisitResponseDto> getVetVisits() {

        UUID currentVetUuid = getCurrentVetUuid();

        return visitRepository.findAllByVetUuid(currentVetUuid)
                .stream()
                .map(visitMapper::toResponse)
                .toList();
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
        return visitRepository.findAll()
                .stream()
                .map(visitMapper::toResponse)
                .toList();
    }

    /**
     * Reschedule a visit to another available slot.
     */
    @Override
    @Transactional
    public void rescheduleVisit(UUID uuid, RescheduleVisitRequestDto request) {
        Visit visit = visitRepository.findByUuidForUpdate(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("visit.not.found", "Visit not found: " + uuid));
        validateCanBeRescheduled(visit);
        Vet vet = getVetWithUuidWithLock(visit.getVet().getUuid());

        DurationTemplateResponseDto standardDuration = durationTemplateService.findByName("STANDARD");

        LocalDateTime newVisitStart = LocalDateTime.of(request.date(), request.startTime());
        LocalDateTime newVisitEnd = getVisitEnd(newVisitStart, standardDuration);

        LocalDateTime oldVisitStart = visit.getStartTime();

        Room newRoom = reserveResources(vet, visit.getVisitType(), newVisitStart, newVisitEnd, visit.getUuid());

        visit.reschedule(newRoom, newVisitStart, newVisitEnd, visit.getVisitType(), request.description());

        visitNotificationService.notifyRescheduleVisitParticipants(
                visit, visit.getPet(), vet, oldVisitStart);

        log.info(
                "Visit rescheduled successfully. visitUuid={}, oldVisitStart={}, newVisitStart={}",
                visit.getUuid(), oldVisitStart, newVisitStart);
    }

    /**
     * Get available slots for a vet on a specific date.
     */
    @Override
    public List<VetAvailableSlotResponseDto> getAvailableSlots(UUID vetUuid, LocalDate date) {
        LocalDate requestedDate = date != null ? date : LocalDate.now();
        validateVetExists(vetUuid);
        appointmentSlotService.generateSlotsForDate(vetUuid, requestedDate);

        return slotRepository
                .findAllByVetUuidAndStatus(vetUuid, SlotStatus.AVAILABLE)
                .stream()
                .map(slot -> new VetAvailableSlotResponseDto(
                        slot.getUuid(),
                        slot.getStartTime().toLocalDate(),
                        slot.getStartTime().toLocalTime(),
                        slot.getEndTime().toLocalTime()
                ))
                .toList();
    }

    private Visit getVisitByUuid(UUID uuid) {
        return visitRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found: " + uuid));
    }

    private Vet getVetByUuid(UUID vetUuid) {
        return vetRepository.findByUuid(vetUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Vet not found: " + vetUuid));
    }

    private Vet getVetWithUuidWithLock(UUID vetUuid) {
        return vetRepository.findByUuidWithLock(vetUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Vet not found: " + vetUuid));
    }

    private void validateVetExists(UUID vetUuid) {
        if (!vetRepository.existsByUuid(vetUuid)) {
            throw new ResourceNotFoundException("Vet not found: " + vetUuid);
        }
    }

    private Room reserveResources(
            Vet vet,
            VisitType visitType,
            LocalDateTime visitStart,
            LocalDateTime visitEnd,
            UUID excludedVisitUuid
    ) {
        validateVisitWindow(visitStart, visitEnd);
        validateVetAvailability(vet, visitStart, visitEnd);
        return allocateRoom(visitType, visitStart, visitEnd, excludedVisitUuid);
    }

    private void validateVisitWindow(LocalDateTime visitStart, LocalDateTime visitEnd) {

        if (visitEnd.isBefore(visitStart)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        if (!visitStart.toLocalDate().equals(visitEnd.toLocalDate())) {
            throw new GenericValidationException(
                    "visit.duration.crosses.date", "A visit must start and end on the same date");
        }
    }

    private LocalDateTime getVisitEnd(LocalDateTime visitStart, DurationTemplateResponseDto duration) {
        return visitStart.plusMinutes(duration.durationMinutes());
    }

    private void validateVetAvailability(Vet vet, LocalDateTime visitStart, LocalDateTime visitEnd) {
        if (!availabilityRepository.existsCoveringTime(vet.getUuid(), visitStart, visitEnd)) {
            throw new ResourceNotFoundException(
                    "visit.vet.not.available", "The selected visit time is outside the vet availability"
            );
        }
    }

    private Room allocateRoom(
            VisitType visitType,
            LocalDateTime visitStart,
            LocalDateTime visitEnd,
            UUID excludedVisitUuid
    ) {
        List<String> roomTypeNames = switch (visitType) {
            case ONSITE -> List.of("examination", "individual");
            case ONLINE, OFFSITE -> List.of();
        };

        if (roomTypeNames.isEmpty()) {
            return null;
        }

        return roomRepository.findActiveRoomsByTypeNamesForUpdate(roomTypeNames)
                .stream()
                .filter(room -> !visitRepository
                        .existsRoomReservation(room, visitStart, visitEnd, excludedVisitUuid))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "visit.room.not.available", "No room is available for the selected visit type and time"));
    }

    private void validateCanBeRescheduled(Visit visit) {
        if (visit.getStatus() == VisitStatus.CANCELLED) {
            throw new ResourceNotFoundException(
                    "visit.cancelled", "Cancelled visit cannot be rescheduled");
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new ResourceNotFoundException(
                    "visit.already.completed", "Completed visit cannot be rescheduled");
        }
    }

    private UUID getCurrentUserUuid(Jwt jwt) {
        return UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
    }

    private UUID getCurrentVetUuid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            return UUID.fromString(Objects.requireNonNull(jwtAuthentication.getToken().getSubject())
            );
        }
        throw new IllegalStateException("Authenticated JWT user not found");
    }
}