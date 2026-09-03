package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.dto.request.VisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetAvailableSlotResponseDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.mapper.VisitMapper;
import com.github.farzan6118.petclinic.model.AppointmentSlot;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.Visit;
import com.github.farzan6118.petclinic.model.constant.SlotStatus;
import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.repository.AppointmentSlotRepository;
import com.github.farzan6118.petclinic.repository.VetRepository;
import com.github.farzan6118.petclinic.repository.VisitRepository;
import com.github.farzan6118.petclinic.service.PetService;
import com.github.farzan6118.petclinic.service.VisitNotificationService;
import com.github.farzan6118.petclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    /**
     * Book an available appointment slot for a pet.
     */
    @Override
    @Transactional
    public UUID bookVisit(VisitRequestDto request) {

        Pet pet = petService.getEntityByUuid(request.petUuid());

        Vet vet = getVetByUuid(request.vetUuid());

        AppointmentSlot slot = slotRepository
                .findAvailableSlotForUpdate(request.slotUuid())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "visit.slot.not.available",
                                "Selected appointment slot is not available"
                        ));

        validateSlotBelongsToVet(slot, vet);

        Visit visit = Visit.create(
                pet,
                vet,
                slot,
                request.description()
        );

        slot.book();

        Visit savedVisit = visitRepository.save(visit);

        visitNotificationService.notifyBookVisitParticipants(
                savedVisit,
                pet,
                vet
        );

        log.info(
                "Visit booked successfully. visitUuid={}, petUuid={}, vetUuid={}, slotUuid={}",
                savedVisit.getUuid(),
                pet.getUuid(),
                vet.getUuid(),
                slot.getUuid()
        );

        return savedVisit.getUuid();
    }

    /**
     * Get the current owner's visits.
     */
    @Override
    public List<VisitResponseDto> getMyVisits(Jwt jwt) {

        UUID currentUserUuid = getCurrentUserUuid(jwt);

        return visitRepository
                .findAllByPetOwnerUuidOrderByAppointmentSlotDateAsc(
                        currentUserUuid
                )
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
            throw new ResourceNotFoundException(
                    "visit.already.completed",
                    "Completed visit cannot be cancelled"
            );
        }

        AppointmentSlot slot = visit.getAppointmentSlot();

        visit.cancel();

        if (slot != null && !slot.isAvailable()) {
            slot.release();
        }

        visitNotificationService.notifyCancelVisitParticipants(
                visit,
                visit.getPet(),
                visit.getVet(),
                reason
        );

        log.info(
                "Visit cancelled. visitUuid={}, slotUuid={}",
                visit.getUuid(),
                slot != null ? slot.getUuid() : null
        );
    }

    /**
     * Get all visits of the currently authenticated vet.
     */
    @Override
    public List<VisitResponseDto> getVetVisits() {

        UUID currentVetUuid = getCurrentVetUuid();

        return visitRepository
                .findAllByVetUuidOrderByAppointmentSlotDateAsc(
                        currentVetUuid
                )
                .stream()
                .map(visitMapper::toResponse)
                .toList();
    }

    /**
     * Complete a visit.
     */
    @Override
    @Transactional
    public VisitResponseDto completeVisit(
            UUID uuid,
            CompleteVisitRequest request
    ) {

        Visit visit = getVisitByUuid(uuid);

        if (visit.getStatus() == VisitStatus.CANCELLED) {
            throw new ResourceNotFoundException(
                    "visit.cancelled",
                    "Cancelled visit cannot be completed"
            );
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new ResourceNotFoundException(
                    "visit.already.completed",
                    "Visit is already completed"
            );
        }

        visit.complete();

        log.info(
                "Visit completed. visitUuid={}",
                visit.getUuid()
        );

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
    public void rescheduleVisit(
            UUID uuid,
            RescheduleVisitRequestDto request
    ) {

        Visit visit = getVisitByUuid(uuid);

        validateCanBeRescheduled(visit);

        AppointmentSlot oldSlot = visit.getAppointmentSlot();

        AppointmentSlot newSlot = slotRepository
                .findAvailableSlotForUpdate(request.slotUuid())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "visit.slot.not.available",
                                "Selected appointment slot is not available"
                        ));

        validateSlotBelongsToVet(
                newSlot,
                visit.getVet()
        );

        /*
         * Release the old slot.
         */
        oldSlot.release();

        /*
         * Book the new slot.
         */
        newSlot.book();

        visit.setAppointmentSlot(newSlot);

        if (request.description() != null) {
            visit.setDescription(request.description());
        }

        visitNotificationService.notifyRescheduleVisitParticipants(
                visit,
                visit.getPet(),
                visit.getVet(),
                null
        );

        log.info(
                "Visit rescheduled successfully. visitUuid={}, oldSlotUuid={}, newSlotUuid={}",
                visit.getUuid(),
                oldSlot.getUuid(),
                newSlot.getUuid()
        );
    }

    /**
     * Get available slots for a vet on a specific date.
     */
    @Override
    public List<VetAvailableSlotResponseDto> getAvailableSlots(
            UUID vetUuid,
            LocalDate date
    ) {

        validateVetExists(vetUuid);

        return slotRepository
                .findAllByVetUuidAndDateAndStatusOrderByStartTime(
                        vetUuid,
                        date,
                        SlotStatus.AVAILABLE
                )
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
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Visit not found: " + uuid
                        ));
    }

    private Vet getVetByUuid(UUID vetUuid) {

        return vetRepository.findByUuid(vetUuid)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vet not found: " + vetUuid
                        ));
    }

    private void validateVetExists(UUID vetUuid) {

        if (!vetRepository.existsByUuid(vetUuid)) {
            throw new ResourceNotFoundException(
                    "Vet not found: " + vetUuid
            );
        }
    }

    private void validateSlotBelongsToVet(
            AppointmentSlot slot,
            Vet vet
    ) {

        if (!slot.getVet().getUuid().equals(vet.getUuid())) {
            throw new ResourceNotFoundException(
                    "visit.slot.invalid.vet",
                    "Selected appointment slot does not belong to the requested vet"
            );
        }
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

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            return UUID.fromString(
                    jwtAuthentication.getToken().getSubject()
            );
        }

        throw new IllegalStateException(
                "Authenticated JWT user not found"
        );
    }
}