package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.model.Owner;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.Visit;
import com.github.farzan6118.petclinic.service.EmailService;
import com.github.farzan6118.petclinic.service.VisitNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitNotificationServiceImpl implements VisitNotificationService {

    private final EmailService emailService;

    @Override
    @Async
    public void notifyVisitRescheduled(Visit visit, Pet pet, Vet vet, LocalDateTime oldVisitDate, String reason) {
        Owner owner = pet.getOwner();

        try {
            emailService.sendVetVisitRescheduledEmail(
                    vet.getEmail(),
                    vet.getFullName(),
                    oldVisitDate,
//                    visit.getVisitDateTime(),
                    null,
                    pet.getName(),
                    pet.getPetType().getName(),
                    pet.getPetType().getBreed(),
                    owner.getFullName()
            );

            emailService.sendOwnerVisitRescheduledEmail(
                    owner.getEmail(),
                    owner.getFullName(),
                    pet.getName(),
                    oldVisitDate,
//                    visit.getVisitDateTime(),
                    null,
                    vet.getFullName()
            );

        } catch (Exception e) {
            log.error("Failed to send visit rescheduled notifications. visitUuid={}", visit.getUuid(), e);
        }
    }

    @Override
    @Async
    public void notifyCancelVisitParticipants(Visit visit, Pet pet, Vet vet, String reason) {
        Owner owner = pet.getOwner();

        try {
            emailService.sendVetVisitCancelledEmail(
                    vet.getEmail(),
                    vet.getFullName(),
//                    visit.getVisitDateTime(),
                    null,
                    pet.getName(),
                    pet.getPetType().getName(),
                    pet.getPetType().getBreed(),
                    owner.getFullName(),
                    reason
            );

            emailService.sendOwnerVisitCancelledEmail(
                    owner.getEmail(),
                    owner.getFullName(),
                    pet.getName(),
//                    visit.getVisitDateTime(),
                    null,
                    vet.getFullName(),
                    reason
            );

        } catch (Exception e) {
            log.error("Failed to send visit cancellation notifications. visitUuid={}", visit.getUuid(), e);
        }
    }

    @Override
    @Async
    public void notifyBookVisitParticipants(Visit visit, Pet pet, Vet vet) {
        Owner owner = pet.getOwner();

        try {
            emailService.sendVetVisitScheduledEmail(
                    vet.getEmail(),
                    vet.getFullName(),
//                    visit.getVisitDateTime(),
                    null,
                    pet.getName(),
                    pet.getPetType().getName(),
                    pet.getPetType().getBreed(),
                    owner.getFullName()
            );

            emailService.sendOwnerVisitScheduledEmail(
                    owner.getEmail(),
                    owner.getFullName(),
                    pet.getName(),
                    pet.getPetType().getName(),
//                    visit.getVisitDateTime(),
                    null,
                    vet.getFullName()
            );

        } catch (Exception e) {
            log.error("Failed to send visit scheduled notifications. visitUuid={}", visit.getUuid(), e);
        }
    }

    @Async
    @Override
    public void notifyRescheduleVisitParticipants(Visit visit, Pet pet, Vet vet, LocalDateTime oldVisitDate) {
        Owner owner = pet.getOwner();

        try {
            emailService.sendVetVisitRescheduledEmail(
                    vet.getEmail(),
                    vet.getFullName(),
                    oldVisitDate,
//                    visit.getVisitDateTime(),
                    null,
                    pet.getName(),
                    pet.getPetType().getName(),
                    pet.getPetType().getBreed(),
                    owner.getFullName()
            );

            emailService.sendOwnerVisitRescheduledEmail(
                    owner.getEmail(),
                    owner.getFullName(),
                    pet.getName(),
                    oldVisitDate,
//                    visit.getVisitDateTime(),
                    null,
                    vet.getFullName()
            );

        } catch (Exception e) {
            log.error("Failed to send visit scheduled notifications. visitUuid={}", visit.getUuid(), e);
        }
    }
}
