package com.github.farzan6118.petclinic.infrastructure.email;

import com.github.farzan6118.petclinic.pet.model.Pet;
import com.github.farzan6118.petclinic.vet.model.Vet;
import com.github.farzan6118.petclinic.visit.model.Visit;

import java.time.LocalDateTime;

public interface VisitNotificationService {
    void notifyVisitRescheduled(Visit visit, Pet pet, Vet vet, LocalDateTime oldVisitDate, String reason);

    void notifyCancelVisitParticipants(Visit visit, Pet pet, Vet vet, String reason);

    void notifyBookVisitParticipants(Visit visit, Pet pet, Vet vet);

    void notifyRescheduleVisitParticipants(Visit visit, Pet pet, Vet vet, LocalDateTime oldVisitDate);
}
