package com.github.farzan6118.petclinic.appointment.service;

import com.github.farzan6118.petclinic.appointment.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.appointment.dto.request.CreateVisitRequestDto;
import com.github.farzan6118.petclinic.appointment.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.appointment.dto.request.VisitAdvancedSearch;
import com.github.farzan6118.petclinic.appointment.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.appointment.model.Visit;
import com.github.farzan6118.petclinic.appointment.repository.VisitRepository;
import com.github.farzan6118.petclinic.clinic.model.Room;
import com.github.farzan6118.petclinic.clinic.service.RoomService;
import com.github.farzan6118.petclinic.common.enums.VisitCategory;
import com.github.farzan6118.petclinic.common.enums.VisitStatus;
import com.github.farzan6118.petclinic.common.enums.VisitType;
import com.github.farzan6118.petclinic.common.exception.BadRequestException;
import com.github.farzan6118.petclinic.common.exception.ConflictException;
import com.github.farzan6118.petclinic.common.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.config.ClinicProperties;
import com.github.farzan6118.petclinic.infrastructure.email.VisitNotificationService;
import com.github.farzan6118.petclinic.pet.model.MedicalRecord;
import com.github.farzan6118.petclinic.pet.model.Pet;
import com.github.farzan6118.petclinic.pet.service.MedicalRecordService;
import com.github.farzan6118.petclinic.pet.service.PetService;
import com.github.farzan6118.petclinic.vet.model.Vet;
import com.github.farzan6118.petclinic.vet.service.VetAvailabilityService;
import com.github.farzan6118.petclinic.vet.service.VetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceCommandImpl implements VisitServiceCommand {

    private final ClinicProperties clinicProperties;
    private final VisitRepository visitRepository;
    private final RoomService roomService;
    private final PetService petService;
    private final VetService vetService;
    private final VetAvailabilityService vetAvailabilityService;
    private final DurationTemplateService durationTemplateService;
    private final VisitNotificationService visitNotificationService;
    private final MedicalRecordService medicalRecordService;

    /**
     * Book an available appointment slot for a pet.
     */
    @Override
    @Transactional
    public void bookVisit(CreateVisitRequestDto request) {
        DurationTemplateResponseDto standardDuration = durationTemplateService.findByName("STANDARD");

        LocalDateTime visitStart = LocalDateTime.of(request.visitDate(), request.visitTime());
        LocalDateTime visitEnd = getVisitEnd(visitStart, standardDuration);

        dateAndTimeValidations(visitStart, visitEnd);

        Vet vet = vetService.getVetWithUuidLock(request.vetUuid());
        Pet pet = petService.getEntityByUuid(request.petUuid());
        Room room = roomService.getAvailableRoomByVisitTypeAndVisitCategory(request.visitType(), VisitCategory.ROUTINE);

        Visit visit = new Visit()
                .schedule(vet, pet, room, visitStart, visitEnd, request.visitType(), request.description());

        validateVisitTime(visitStart, visitEnd);
        vetAvailabilityValidation(vet, visitStart, visitEnd, null);
        validateRoomAvailability(room, visitStart, visitEnd, null);
        petAvailabilityValidation(pet, visitStart, visitEnd, null);

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

    private void vetAvailabilityValidation(Vet vet, LocalDateTime visitStart, LocalDateTime visitEnd, UUID visitUuid) {
        if (vet == null) {
            return;
        }

        vetAvailabilityService.findAvailableByUuidAndTimeRange(vet.getUuid(), visitStart, visitEnd)
                .orElseThrow(() -> new ConflictException(
                        "the vet is not available in this date time",
                        "the vet is not available in this date time")
                );

        boolean existsVetReservation = visitRepository.existsVetReservation(
                vet.getUuid(), visitStart, visitEnd, visitUuid);

        if (existsVetReservation) {
            throw new ConflictException(
                    "vet is busy in this time",
                    "vet is busy in this time");
        }
    }

    private void validateVisitTime(LocalDateTime visitStart, LocalDateTime visitEnd) {
        LocalDate visitDate = visitStart.toLocalDate();

        if (clinicProperties.closeDays().contains(visitDate.getDayOfWeek())) {
            String message = String.format("the clinic is closed on %s (%s)", visitDate.getDayOfWeek(), visitDate);
            throw new BadRequestException(message, message);
        }

        ClinicProperties.WorkingHours workingHours = clinicProperties.workingHours();

        if (visitStart.toLocalTime().isBefore(workingHours.start()) ||
                visitEnd.toLocalTime().isAfter(workingHours.end())) {

            String message = String.format("the visit must be scheduled between %s and %s on %s",
                    workingHours.start(), workingHours.end(), visitDate);

            throw new BadRequestException(message, message);
        }
    }

    private void validateRoomAvailability(Room room, LocalDateTime visitStart, LocalDateTime visitEnd, UUID visitUuid) {
        if (room == null) {
            return;
        }

        boolean existsRoomReservation = visitRepository.existsRoomReservation(
                room.getUuid(), visitStart, visitEnd, visitUuid);

        if (existsRoomReservation) {
            String message = String.format("the room %s is already booked from %s to %s",
                    room.getName(), visitStart, visitEnd);

            throw new ConflictException(message, message);
        }
    }

    private void petAvailabilityValidation(Pet pet, LocalDateTime visitStart, LocalDateTime visitEnd, UUID visitUuid) {
        boolean existsPetReservation = visitRepository.existsPetReservation(
                pet.getUuid(), visitStart, visitEnd, visitUuid);

        if (existsPetReservation) {
            String message = String.format("the pet already has an appointment from %s to %s",
                    visitStart, visitEnd);

            throw new ConflictException(message, message);
        }
    }

    private void dateAndTimeValidations(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new BadRequestException(
                    "visit visitDateTo time must be after visitDateFrom time",
                    "visit visitDateTo time must be after visitDateFrom time");
        }

        if (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            throw new BadRequestException(
                    "visit visitDateFrom and visitDateTo time must be on the same day",
                    "visit visitDateFrom and visitDateTo time must be on the same day");
        }
    }

    @Override
    @Transactional
    public void cancelVisit(UUID uuid, String reason) {

        Visit visit = getVisitByUuid(uuid);

        if (visit.getStatus() == VisitStatus.CANCELLED) {
            return;
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new ConflictException("visit.already.completed", "Completed visit cannot be cancelled");
        }

        visit.cancel();

        visitNotificationService.notifyCancelVisitParticipants(visit, visit.getPet(), visit.getVet(), reason);

        log.info("Visit cancelled. visitUuid={}", visit.getUuid());
    }

    private void dateTimeValidation(VisitAdvancedSearch request) {
        if (request.createdDateFrom() != null && request.createdDateTo() != null) {
            if (request.createdDateFrom().isAfter(request.createdDateTo())) {
                throw new BadRequestException(
                        "invalid.created.date.from.created.date.to",
                        "create date from is after create date to"
                );
            }
        }
        if (request.visitDateFrom() != null && request.visitDateTo() != null) {
            if (request.visitDateFrom().isAfter(request.visitDateTo())) {
                throw new BadRequestException(
                        "invalid.visit.date.from.visit.date.to",
                        "visit date from is after visit date to"
                );
            }
        }
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

        if (request != null) {
            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecordService.create(medicalRecord, request, visit);
        }

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

            case CANCELLED -> throw new ConflictException("visit.cancelled", "Cancelled visit cannot be completed");

            case COMPLETED -> throw new ConflictException("visit.already.completed", "Visit is already completed");

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
            throw new BadRequestException("visit.not.started", "Visit has not started yet");
        }

        if (now.isAfter(endTime)) {
            throw new BadRequestException("visit.already.finished", "Visit has already finished");
        }
    }

    /**
     * Reschedule a visit to another available slot.
     */
    @Override
    @Transactional
    public void rescheduleVisit(UUID uuid, RescheduleVisitRequestDto request) {
        Visit visit = visitRepository.findByUuidForUpdate(uuid).orElseThrow(
                () -> new ResourceNotFoundException("visit.not.found", "Visit not found: " + uuid));

        DurationTemplateResponseDto standardDuration = durationTemplateService.findByName("STANDARD");
        LocalDateTime newVisitStart = LocalDateTime.of(request.visitDate(), request.visitTime());
        LocalDateTime newVisitEnd = getVisitEnd(newVisitStart, standardDuration);

        dateAndTimeValidations(newVisitStart, newVisitEnd);
        Pet pet = visit.getPet();
        VisitType visitType = request.visitType() != null ? request.visitType() : visit.getVisitType();
        Vet vet = vetService.getVetWithUuidLock(visit.getVet().getUuid());

        LocalDateTime oldVisitStart = visit.getStartTime();

        Room newRoom = roomService.getAvailableRoomByVisitTypeAndVisitCategory(visitType, VisitCategory.ROUTINE);

        visit.reschedule(newRoom, newVisitStart, newVisitEnd, visitType, request.description());

        validateVisitTime(newVisitStart, newVisitEnd);
        vetAvailabilityValidation(vet, newVisitStart, newVisitEnd, visit.getUuid());
        validateRoomAvailability(newRoom, newVisitStart, newVisitEnd, visit.getUuid());
        petAvailabilityValidation(pet, newVisitStart, newVisitEnd, visit.getUuid());

        visitNotificationService.notifyRescheduleVisitParticipants(oldVisitStart, visit, pet, vet, newVisitStart);
        visitRepository.save(visit);
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

}
