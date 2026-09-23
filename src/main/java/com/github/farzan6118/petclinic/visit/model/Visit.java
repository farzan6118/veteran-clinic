package com.github.farzan6118.petclinic.visit.model;

import com.github.farzan6118.petclinic.common.enums.VisitStatus;
import com.github.farzan6118.petclinic.common.enums.VisitType;
import com.github.farzan6118.petclinic.common.exception.ForbiddenException;
import com.github.farzan6118.petclinic.common.persistence.BaseEntity;
import com.github.farzan6118.petclinic.pet.model.Pet;
import com.github.farzan6118.petclinic.room.model.Room;
import com.github.farzan6118.petclinic.vet.model.Vet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Audited;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Audited
@NoArgsConstructor
public class Visit extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitType visitType;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitStatus status;

    @Column(length = 1000)
    private String description;

    private Instant bookedAt;


    public Visit schedule(Vet vet, Pet pet, Room room, LocalDateTime startTime, LocalDateTime endTime,
                          VisitType visitType, String description) {
        dateAndTimeValidations(startTime, endTime);
        Visit visit = new Visit();
        visit.vet = vet;
        visit.pet = pet;
        visit.room = room;
        visit.startTime = startTime;
        visit.endTime = endTime;
        visit.visitType = visitType;
        visit.description = description;
        visit.status = VisitStatus.SCHEDULED;
        visit.bookedAt = Instant.now();
        return visit;
    }

    public void reschedule(Room room, LocalDateTime startTime, LocalDateTime endTime,
                           VisitType visitType, String description) {
        dateAndTimeValidations(startTime, endTime);
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.visitType = visitType;

        if (description != null) {
            this.description = description;
        }
    }

    public void cancel() {

        if (status == VisitStatus.COMPLETED) {
            throw new ForbiddenException("Completed visit cannot be cancelled");
        }

        if (status == VisitStatus.CANCELLED) {
            return;
        }

        this.status = VisitStatus.CANCELLED;
    }

    public void complete(LocalDateTime end) {

        if (status == VisitStatus.CANCELLED) {
            throw new ForbiddenException("Cancelled visit cannot be completed");
        }

        if (status == VisitStatus.COMPLETED) {
            throw new ForbiddenException("Visit is already completed");
        }

        this.endTime = end;
        this.status = VisitStatus.COMPLETED;
    }

    public void dateAndTimeValidations(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before visitDateFrom time");
        }

        if (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            throw new IllegalArgumentException("the visitDateFrom and visitDateTo time must be the same day");
        }
    }
}