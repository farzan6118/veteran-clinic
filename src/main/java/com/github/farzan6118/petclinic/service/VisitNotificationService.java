package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.Visit;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

public interface VisitNotificationService {
    @Async("emailExecutor")
    void notifyVisitRescheduled(Visit visit, Pet pet, Vet vet, LocalDateTime oldVisitDate, String reason);

    @Async("emailExecutor")
    void notifyCancelVisitParticipants(Visit visit, Pet pet, Vet vet, String reason);

    @Async("emailExecutor")
    void notifyBookVisitParticipants(Visit visit, Pet pet, Vet vet);
}
