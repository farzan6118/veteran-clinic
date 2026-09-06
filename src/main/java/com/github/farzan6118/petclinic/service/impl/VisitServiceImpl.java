package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.dto.request.VisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetAvailableSlotResponseDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.mapper.VisitMapper;
import com.github.farzan6118.petclinic.model.*;
import com.github.farzan6118.petclinic.model.constant.AppointmentType;
import com.github.farzan6118.petclinic.model.constant.SlotStatus;
import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.model.constant.VisitType;
import com.github.farzan6118.petclinic.repository.*;
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

        Vet vet = getVetByUuid(request.vetUuid());

        AppointmentSlot slot = slotRepository
                .findAvailableSlotForUpdate(request.vetUuid(), request.visitDate(), request.visitTime())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "visit.slot.not.available", "Selected appointment slot is not available"));

        validateSlotBelongsToVet(slot, vet);

        LocalDateTime visitStart = LocalDateTime.of(request.visitDate(), request.visitTime());
        if (!visitStart.isAfter(LocalDateTime.now())) {
            throw new ResourceNotFoundException(
                    "visit.time.must.be.in.future",
                    "The visit date and time must be in the future"
            );
        }
        LocalDateTime visitEnd = visitStart.plusMinutes(getDurationMinutes(request.visitType()));
        validateVetAvailability(vet, visitStart, visitEnd);
        validateVetIsFree(vet, visitStart, visitEnd, null);
        Room room = allocateRoom(request.visitType(), visitStart, visitEnd, null);

        Visit visit = Visit.create(
                pet,
                vet,
                slot,
                room,
                request.visitType(),
                visitStart,
                visitEnd,
                request.description()
        );

        slot.book();
        slot.setAppointmentType(toAppointmentType(request.visitType()));

        Visit savedVisit = visitRepository.save(visit);

        visitNotificationService.notifyBookVisitParticipants(savedVisit, pet, vet);

        log.info("Visit booked successfully. visitUuid={}, petUuid={}, vetUuid={}, slotUuid={}",
                savedVisit.getUuid(), pet.getUuid(), vet.getUuid(), slot.getUuid());

        return savedVisit.getUuid();
    }

    /**
     * Get the current owner's visits.
     */
    @Override
    public List<VisitResponseDto> getMyVisits(Jwt jwt) {

        UUID currentUserUuid = getCurrentUserUuid(jwt);

        return visitRepository.findAllByPetOwnerUuidOrderByAppointmentSlotDateAsc(currentUserUuid)
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

        AppointmentSlot slot = visit.getAppointmentSlot();

        visit.cancel();

        if (slot != null && !slot.isAvailable()) {
            slot.release();
        }

        visitNotificationService.notifyCancelVisitParticipants(visit, visit.getPet(), visit.getVet(), reason);

        log.info(
                "Visit cancelled. visitUuid={}, slotUuid={}",
                visit.getUuid(), slot != null ? slot.getUuid() : null
        );
    }

    /**
     * Get all visits of the currently authenticated vet.
     */
    @Override
    public List<VisitResponseDto> getVetVisits() {

        UUID currentVetUuid = getCurrentVetUuid();

        return visitRepository.findAllByVetUuidOrderByAppointmentSlotDateAsc(currentVetUuid)
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
        Visit visit = getVisitByUuid(uuid);
        if (visit.getStatus() == VisitStatus.CANCELLED) {
            throw new ResourceNotFoundException("visit.cancelled", "Cancelled visit cannot be completed");
        }
        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new ResourceNotFoundException("visit.already.completed", "Visit is already completed");
        }
        visit.complete();
        log.info("Visit completed. visitUuid={}", visit.getUuid());
        return visitMapper.toResponse(visit);
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
        Visit visit = getVisitByUuid(uuid);
        validateCanBeRescheduled(visit);
        if (request.slotUuid() == null) {
            throw new ResourceNotFoundException(
                    "visit.slot.is.required",
                    "A replacement appointment slot is required"
            );
        }
        AppointmentSlot oldSlot = visit.getAppointmentSlot();
        LocalDateTime oldVisitStart = visit.getVisitStart();

        AppointmentSlot newSlot = slotRepository
                .findAvailableSlotForUpdate(request.slotUuid())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "visit.slot.not.available",
                                "Selected appointment slot is not available"
                        ));

        validateSlotBelongsToVet(newSlot, visit.getVet());

        LocalDateTime newVisitStart = LocalDateTime.of(newSlot.getDate(), newSlot.getStartTime());
        LocalDateTime newVisitEnd = newVisitStart.plusMinutes(getDurationMinutes(visit.getVisitType()));
        validateVetAvailability(visit.getVet(), newVisitStart, newVisitEnd);
        validateVetIsFree(visit.getVet(), newVisitStart, newVisitEnd, visit.getUuid());
        Room newRoom = allocateRoom(visit.getVisitType(), newVisitStart, newVisitEnd, visit.getUuid());

        /*
         * Release the old slot.
         */
        oldSlot.release();

        /*
         * Book the new slot.
         */
        newSlot.book();
        newSlot.setAppointmentType(toAppointmentType(visit.getVisitType()));

        visit.setAppointmentSlot(newSlot);
        visit.setRoom(newRoom);
        visit.setVisitStart(newVisitStart);
        visit.setVisitEnd(newVisitEnd);

        if (request.description() != null) {
            visit.setDescription(request.description());
        }

        visitNotificationService.notifyRescheduleVisitParticipants(
                visit, visit.getPet(), visit.getVet(), oldVisitStart);

        log.info(
                "Visit rescheduled successfully. visitUuid={}, oldSlotUuid={}, newSlotUuid={}",
                visit.getUuid(), oldSlot.getUuid(), newSlot.getUuid());
    }

    /**
     * Get available slots for a vet on a specific date.
     */
    @Override
    public List<VetAvailableSlotResponseDto> getAvailableSlots(UUID vetUuid, LocalDate date) {

        validateVetExists(vetUuid);

        return slotRepository
                .findAllByVetUuidAndDateAndStatusOrderByStartTime(vetUuid, date, SlotStatus.AVAILABLE)
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

    private void validateVetExists(UUID vetUuid) {
        if (!vetRepository.existsByUuid(vetUuid)) {
            throw new ResourceNotFoundException("Vet not found: " + vetUuid);
        }
    }

    private void validateSlotBelongsToVet(AppointmentSlot slot, Vet vet) {
        if (!slot.getVet().getUuid().equals(vet.getUuid())) {
            throw new ResourceNotFoundException(
                    "visit.slot.invalid.vet",
                    "Selected appointment slot does not belong to the requested vet"
            );
        }
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
        if (visitRepository.existsVetReservation(vet.getUuid(), visitStart, visitEnd, excludedVisitUuid)) {
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
                        room, visitStart, visitEnd, excludedVisitUuid))
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

    private AppointmentType toAppointmentType(VisitType visitType) {
        return switch (visitType) {
            case ONSITE, EMERGENCY -> AppointmentType.IN_CLINIC;
            case ONLINE -> AppointmentType.ONLINE;
            case OWNERS_PLACE -> AppointmentType.HOME_VISIT;
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