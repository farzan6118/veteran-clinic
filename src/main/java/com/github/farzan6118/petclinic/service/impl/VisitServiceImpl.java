package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.CreateVisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.mapper.VisitMapper;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.Visit;
import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.repository.jpa.PetRepository;
import com.github.farzan6118.petclinic.repository.jpa.VetRepository;
import com.github.farzan6118.petclinic.repository.jpa.VisitRepository;
import com.github.farzan6118.petclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final VisitMapper visitMapper;

    @Override
    @Transactional
    public VisitResponseDto bookVisit(CreateVisitRequestDto request) {

        Pet pet = petRepository.findByUuid(request.petUuid())
                .orElseThrow(() -> new RuntimeException("Pet not found: " + request.petUuid()));

        Vet vet = vetRepository.findByUuid(request.vetUuid())
                .orElseThrow(() -> new RuntimeException("Vet not found: " + request.vetUuid()));

        boolean vetHasVisit = visitRepository.existsByVetUuidAndVisitDateTime(
                request.vetUuid(),
                request.visitDateTime()
        );

        if (vetHasVisit) {
            throw new RuntimeException("Vet is already booked at this time");
        }

        Visit visit = visitMapper.mapToVisitEntity(request, pet, vet);

        Visit savedVisit = visitRepository.save(visit);

        log.info("Visit booked successfully. visitUuid={}, petUuid={}, vetUuid={}",
                savedVisit.getUuid(), pet.getUuid(), vet.getUuid()
        );

        return visitMapper.toResponse(savedVisit);
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

        Visit visit = visitRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Visit not found: " + uuid));

        return visitMapper.toResponse(visit);
    }

    @Override
    @Transactional
    public void cancelVisit(UUID uuid) {

        Visit visit = visitRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Visit not found: " + uuid));

        if (visit.getStatus() == VisitStatus.CANCELLED) {
            return;
        }

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new RuntimeException("Completed visit cannot be cancelled");
        }

        visit.setStatus(VisitStatus.CANCELLED);

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

        Visit visit = visitRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Visit not found: " + uuid));

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

