package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.dto.request.VisitRequestDto;
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
import com.github.farzan6118.petclinic.service.AppointmentSlotService;
import com.github.farzan6118.petclinic.service.PetService;
import com.github.farzan6118.petclinic.service.VisitNotificationService;
import com.github.farzan6118.petclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @Value("${clinic.scheduling.standard-duration-minutes:10}")
    private int standardDurationMinutes;

    @Value("${clinic.scheduling.owners-place-duration-minutes:60}")
    private int ownersPlaceDurationMinutes;

    @Value("${clinic.scheduling.emergency-duration-minutes:30}")
    private int emergencyDurationMinutes;

    /**
     * Book an available appointment slot for a pet.
     */
    @Override
    @Transactional
    public UUID bookVisit(VisitRequestDto request) {

        Pet pet = petService.getEntityByUuid(request.petUuid());

        Vet vet = getVetForUpdate(request.vetUuid());

        LocalDateTime visitStart = LocalDateTime.of(request.visitDate(), request.visitTime());
        if (!visitStart.isAfter(LocalDateTime.now())) {
            throw new ResourceNotFoundException(
                    "visit.time.must.be.in.future",
                    "The visit date and time must be in the future"
            );
        }
        LocalDateTime visitEnd = getVisitEnd(visitStart, request.visitType());
        Room room = reserveResources(vet, request.visitType(), visitStart, visitEnd, null);

        Visit visit = Visit.create(
                vet,
                pet,
                room,
                visitStart.toLocalDate(),
                visitStart.toLocalTime(),
                visitEnd.toLocalTime(),
                request.visitType(),
                request.description()
        );

        Visit savedVisit = visitRepository.save(visit);

        visitNotificationService.notifyBookVisitParticipants(savedVisit, pet, vet);

        log.info("Visit booked successfully. visitUuid={}, petUuid={}, vetUuid={}",
                savedVisit.getUuid(), pet.getUuid(), vet.getUuid());

        return savedVisit.getUuid();
    }

    /**
     * Get the current owner's visits.
     */
    @Override
    public List<VisitResponseDto> getMyVisits(Jwt jwt) {

        UUID currentUserUuid = getCurrentUserUuid(jwt);

        return visitRepository.findAllByPetOwnerUuidOrderByDateAscStartTimeAsc(currentUserUuid)
                .stream()
                .map(visitMapper::toResponse)
                .toList();
    }

    /**
     * Get a visit by UUID.
     */
    @Override
    public VisitResponseDto getByUuid(UUID uuid) {

        Visit visit = getVisitByUuid(uuid);

        return visitMapper.toResponse(visit);
    }

    /**
     * Cancel a scheduled visit.
     */
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

        log.info(
                "Visit cancelled. visitUuid={}",
                visit.getUuid()
        );
    }

    /**
     * Get all visits of the currently authenticated vet.
     */
    @Override
    public List<VisitResponseDto> getVetVisits() {

        UUID currentVetUuid = getCurrentVetUuid();

        return visitRepository.findAllByVetUuidOrderByDateAscStartTimeAsc(currentVetUuid)
                .stream()
                .map(visitMapper::toResponse)
                .toList();
    }

    /**
     * Complete a visit.
     */
    @Override
    @Transactional
    public VisitResponseDto completeVisit(UUID uuid, CompleteVisitRequest request) {

        Visit visit = getVisitForUpdate(uuid);

        validateCompletion(visit);

        visit.complete();

        log.info("Visit completed successfully. visitUuid={}", uuid);

        return visitMapper.toResponse(visit);
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

        LocalDateTime startDateTime = LocalDateTime.of(visit.getDate(), visit.getStartTime());

        LocalDateTime endDateTime = LocalDateTime.of(visit.getDate(), visit.getEndTime());

        if (now.isBefore(startDateTime)) {
            throw new GenericValidationException("visit.not.started", "Visit has not started yet");
        }

        if (now.isAfter(endDateTime)) {
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

        Visit visit = visitRepository.findByUuidForUpdate(uuid).orElseThrow(() ->
                new ResourceNotFoundException("visit.not.found", "Visit not found: " + uuid)
        );

        validateCanBeRescheduled(visit);

        Vet vet = getVetForUpdate(visit.getVet().getUuid());

        LocalDateTime oldVisitStart = LocalDateTime.of(visit.getDate(), visit.getStartTime());

        LocalDateTime newVisitStart = LocalDateTime.of(request.Date(), request.startTime());

        if (!newVisitStart.isAfter(LocalDateTime.now())) {
            throw new ResourceNotFoundException(
                    "visit.time.must.be.in.future", "The visit date and time must be in the future");
        }

        LocalDateTime newVisitEnd = getVisitEnd(newVisitStart, visit.getVisitType());

        if (!newVisitEnd.isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException(
                    "visit.time.must.be.in.future", "The visit date and time must be in the future");
        }

        Room newRoom = reserveResources(vet, visit.getVisitType(), newVisitStart, newVisitEnd, visit.getUuid());

        visit.reschedule(newVisitStart.toLocalDate(), newVisitStart.toLocalTime(),
                newVisitEnd.toLocalTime(), newRoom, request.description());

        visitNotificationService.notifyRescheduleVisitParticipants(visit, visit.getPet(), vet, oldVisitStart);

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
                .findAllByVetUuidAndDateAndStatusOrderByStartTime(vetUuid, requestedDate, SlotStatus.AVAILABLE)
                .stream()
                .map(slot -> new VetAvailableSlotResponseDto(
                        slot.getUuid(),
                        slot.getDate(),
                        slot.getStartTime(),
                        slot.getEndTime()
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

    private Vet getVetForUpdate(UUID vetUuid) {
        return vetRepository.findByUuidForUpdate(vetUuid)
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
        validateVetIsFree(vet, visitStart, visitEnd, excludedVisitUuid);
        return allocateRoom(visitType, visitStart, visitEnd, excludedVisitUuid);
    }

    private void validateVisitWindow(LocalDateTime visitStart, LocalDateTime visitEnd) {
        if (!visitEnd.toLocalDate().equals(visitStart.toLocalDate())) {
            throw new ResourceNotFoundException(
                    "visit.duration.crosses.date",
                    "A visit must start and end on the same date"
            );
        }
    }

    private LocalDateTime getVisitEnd(LocalDateTime visitStart, VisitType visitType) {
        return visitStart.plusMinutes(getDurationMinutes(visitType));
    }

    private void validateVetAvailability(Vet vet, LocalDateTime visitStart, LocalDateTime visitEnd) {
        if (!availabilityRepository.existsCoveringTime(
                vet.getUuid(),
                visitStart.toLocalDate(),
                visitStart.toLocalTime(),
                visitEnd.toLocalTime())) {
            throw new ResourceNotFoundException(
                    "visit.vet.not.available",
                    "The selected visit time is outside the vet availability"
            );
        }
    }

    private void validateVetIsFree(Vet vet, LocalDateTime visitStart, LocalDateTime visitEnd, UUID excludedVisitUuid) {
        if (visitRepository.existsVetReservation(
                vet.getUuid(),
                visitStart.toLocalDate(),
                visitStart.toLocalTime(),
                visitEnd.toLocalTime(),
                excludedVisitUuid
        )) {
            throw new ResourceNotFoundException(
                    "visit.vet.time.not.available",
                    "The vet is already booked during the selected time"
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
            case EMERGENCY -> List.of("surgery", "emergency", "isolation");
            case ONLINE, OWNERS_PLACE -> List.of();
        };

        if (roomTypeNames.isEmpty()) {
            return null;
        }

        return roomRepository.findActiveRoomsByTypeNamesForUpdate(roomTypeNames)
                .stream()
                .filter(room -> !visitRepository.existsRoomReservation(
                        room,
                        visitStart.toLocalDate(),
                        visitStart.toLocalTime(),
                        visitEnd.toLocalTime(),
                        excludedVisitUuid))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "visit.room.not.available",
                        "No room is available for the selected visit type and time"
                ));
    }

    private int getDurationMinutes(VisitType visitType) {
        return switch (visitType) {
            case ONSITE, ONLINE -> standardDurationMinutes;
            case OWNERS_PLACE -> ownersPlaceDurationMinutes;
            case EMERGENCY -> emergencyDurationMinutes;
        };
    }

    private void validateCanBeRescheduled(Visit visit) {
        if (visit.getStatus() == VisitStatus.CANCELLED) {
            throw new ResourceNotFoundException(
                    "visit.cancelled",
                    "Cancelled visit cannot be rescheduled"
            );
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new ResourceNotFoundException(
                    "visit.already.completed",
                    "Completed visit cannot be rescheduled"
            );
        }
    }

    private UUID getCurrentUserUuid(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    private UUID getCurrentVetUuid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            return UUID.fromString(jwtAuthentication.getToken().getSubject()
            );
        }
        throw new IllegalStateException("Authenticated JWT user not found");
    }
}