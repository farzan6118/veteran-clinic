package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.dto.request.VisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.mapper.VisitMapper;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.VetAvailability;
import com.github.farzan6118.petclinic.model.Visit;
import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.repository.VisitRepository;
import com.github.farzan6118.petclinic.service.PetService;
import com.github.farzan6118.petclinic.service.VetService;
import com.github.farzan6118.petclinic.service.VisitNotificationService;
import com.github.farzan6118.petclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitService {

    private final VisitNotificationService visitNotificationService;
    private final VisitRepository visitRepository;
    private final PetService petService;
    private final VetService vetService;
    private final VisitMapper visitMapper;

    @Override
    @Transactional
    public void bookVisit(VisitRequestDto request) {

        Pet pet = petService.getEntityByUuid(request.petUuid());
        Vet vet = vetService.getEntityByUuid(request.vetUuid());
        validateVetAvailability(vet, request.visitDateTime());

        Visit visit = visitMapper.mapToVisitEntity(request, pet, vet);
        Visit savedVisit = visitRepository.save(visit);

        visitNotificationService.notifyBookVisitParticipants(savedVisit, pet, vet);

        log.info(
                "Visit booked successfully. visitUuid={}, petUuid={}, vetUuid={}",
                savedVisit.getUuid(),
                pet.getUuid(),
                vet.getUuid()
        );
    }

    private void validateVetAvailability(Vet vet, LocalDateTime visitDateTime) {

        DayOfWeek dayOfWeek = visitDateTime.getDayOfWeek();
        LocalTime time = visitDateTime.toLocalTime();

        boolean available = vet.getAvailabilities()
                .stream()
                .filter(VetAvailability::isActive)
                .filter(availability -> availability.getDayOfWeek() == dayOfWeek)
                .anyMatch(availability ->
                        !time.isBefore(availability.getStartTime())
                                && time.isBefore(availability.getEndTime())
                );

        if (!available) {
            throw new ResourceNotFoundException("Vet is not available at the requested time");
        }

        boolean alreadyBooked = visitRepository.existsByVetUuidAndVisitDateTime(vet.getUuid(), visitDateTime);

        if (alreadyBooked) {
            throw new ResourceNotFoundException("Vet is already booked at the requested time");
        }
    }

    @Override
    public List<VisitResponseDto> getMyVisits() {

        UUID currentUserUuid = getCurrentUserUuid();

        return visitRepository
                .findAllByPetOwnerUuidOrderByVisitDateTimeDesc(currentUserUuid)
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
            throw new ResourceNotFoundException("Completed visit cannot be cancelled");
        }


        visit.setStatus(VisitStatus.CANCELLED);

        Pet pet = visit.getPet();
        Vet vet = visit.getVet();

        visitNotificationService.notifyCancelVisitParticipants(visit, pet, vet, reason);

        log.info("Visit cancelled. visitUuid={}", uuid);
    }

    @Override
    public List<VisitResponseDto> getVetVisits() {

        UUID currentVetUuid = getCurrentVetUuid();

        return visitRepository
                .findAllByVetUuidOrderByVisitDateTimeAsc(currentVetUuid)
                .stream()
                .map(visitMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public VisitResponseDto completeVisit(UUID uuid, CompleteVisitRequest request) {

        Visit visit = getVisitByUuid(uuid);

        if (visit.getStatus() == VisitStatus.CANCELLED) {
            throw new ResourceNotFoundException("Cancelled visit cannot be completed");
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new ResourceNotFoundException("Visit is already completed");
        }

        visit.setStatus(VisitStatus.COMPLETED);
        visit.setDiagnosis(request.diagnosis());
        visit.setNotes(request.notes());

        Visit savedVisit = visitRepository.save(visit);

        log.info("Visit completed. visitUuid={}", uuid);

        return visitMapper.toResponse(savedVisit);
    }

    @Override
    public List<VisitResponseDto> getAllVisits() {
        List<Visit> allVisits = visitRepository.findAll();
        return allVisits.stream()
                .map(visitMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void rescheduleVisit(UUID uuid, RescheduleVisitRequestDto request) {
        Visit visit = visitRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("visit not found"));

        if (visit.getStatus() == VisitStatus.CANCELLED) {
            throw new ResourceNotFoundException("Cancelled visit cannot be completed");
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new ResourceNotFoundException("Visit is already completed");
        }

        Pet pet = visit.getPet();
        Vet vet = visit.getVet();

        validateVetAvailability(vet, request.visitDateTime());
        LocalDateTime oldVisitDate = visit.getVisitDateTime();
        visit.setVisitDateTime(request.visitDateTime());
        visit.setDescription(request.description());

        Visit savedVisit = visitRepository.save(visit);

        visitNotificationService.notifyRescheduleVisitParticipants(savedVisit, pet, vet, oldVisitDate);

        log.info(
                "Visit rescheduled to dateAndTime={} successfully. visitUuid={}",
                savedVisit.getVisitDateTime(),
                savedVisit.getUuid()
        );
    }

    private Visit getVisitByUuid(UUID uuid) {
        return visitRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found: " + uuid));
    }

    private UUID getCurrentUserUuid() {
        // TODO:
        // Get current authenticated user from your SecurityContext
        throw new ResourceNotFoundException("Current user resolver is not implemented");
    }

    private UUID getCurrentVetUuid() {
        // TODO:
        // Get current authenticated vet from your SecurityContext
        throw new ResourceNotFoundException("Current vet resolver is not implemented");
    }

}

