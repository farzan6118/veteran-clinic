package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.Visit;

import java.time.LocalDateTime;

public interface VisitNotificationService {
    void notifyVisitRescheduled(Visit visit, Pet pet, Vet vet, LocalDateTime oldVisitDate, String reason);

    void notifyCancelVisitParticipants(Visit visit, Pet pet, Vet vet, String reason);

    void notifyBookVisitParticipants(Visit visit, Pet pet, Vet vet);

    void notifyRescheduleVisitParticipants(Visit visit, Pet pet, Vet vet, LocalDateTime oldVisitDate);
}
