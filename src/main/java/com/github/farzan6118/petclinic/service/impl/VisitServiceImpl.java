package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.CreateVisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.mapper.VisitMapper;
import com.github.farzan6118.petclinic.model.Owner;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.Visit;
import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.repository.jpa.VisitRepository;
import com.github.farzan6118.petclinic.service.EmailService;
import com.github.farzan6118.petclinic.service.PetService;
import com.github.farzan6118.petclinic.service.VetService;
import com.github.farzan6118.petclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PetService petService;
    private final VetService vetService;
    private final VisitMapper visitMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public void bookVisit(CreateVisitRequestDto request) {

        Pet pet = petService.getByUuid(request.petUuid());
        Vet vet = vetService.getVetByUuid(request.vetUuid());

        validateVetAvailability(vet, request.visitDateTime());

        Visit visit = visitMapper.mapToVisitEntity(request, pet, vet);
        Visit savedVisit = visitRepository.save(visit);

        notifyBookVisitParticipants(savedVisit, pet, vet);

        log.info(
                "Visit booked successfully. visitUuid={}, petUuid={}, vetUuid={}",
                savedVisit.getUuid(),
                pet.getUuid(),
                vet.getUuid()
        );
    }

    private void validateVetAvailability(Vet vet, LocalDateTime visitDateTime) {
        boolean alreadyBooked = visitRepository
                .existsByVetUuidAndVisitDateTime(vet.getUuid(), visitDateTime);

        if (alreadyBooked) {
            throw new RuntimeException("Vet is already booked at the requested time");
        }
    }

    private void notifyBookVisitParticipants(Visit visit, Pet pet, Vet vet) {
        Owner owner = pet.getOwner();

        emailService.sendVetAppointmentScheduledNotification(
                vet.getEmail(),
                vet.getFullName(),
                visit.getVisitDateTime(),
                pet.getName(),
                pet.getPetType().getName(),
                owner.getFullName()
        );

        emailService.sendVisitScheduledNotification(
                owner.getEmail(),
                owner.getFullName(),
                pet.getName(),
                visit.getVisitDateTime(),
                vet.getFullName()
        );
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
            throw new RuntimeException("Completed visit cannot be cancelled");
        }


        visit.setStatus(VisitStatus.CANCELLED);

        Pet pet = visit.getPet();
        Vet vet = visit.getVet();

        notifyCancelVisitParticipants(visit, pet, vet, reason);

        log.info("Visit cancelled. visitUuid={}", uuid);
    }

    private void notifyCancelVisitParticipants(Visit visit, Pet pet, Vet vet, String reason) {
        Owner owner = pet.getOwner();

        emailService.sendVetAppointmentCancelledNotification(
                vet.getEmail(),
                vet.getFullName(),
                pet.getName(),
                owner.getFullName(),
                visit.getVisitDateTime(),
                reason
        );

        emailService.sendVisitCancelledNotification(
                owner.getEmail(),
                owner.getFullName(),
                pet.getName(),
                pet.getPetType().getName(),
                vet.getFullName(),
                visit.getVisitDateTime(),
                reason
        );
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
            throw new RuntimeException("Cancelled visit cannot be completed");
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new RuntimeException("Visit is already completed");
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

    private Visit getVisitByUuid(UUID uuid) {
        return visitRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Visit not found: " + uuid));
    }

    private UUID getCurrentUserUuid() {
        // TODO:
        // Get current authenticated user from your SecurityContext
        throw new RuntimeException("Current user resolver is not implemented");
    }

    private UUID getCurrentVetUuid() {
        // TODO:
        // Get current authenticated vet from your SecurityContext
        throw new RuntimeException("Current vet resolver is not implemented");
    }

}

