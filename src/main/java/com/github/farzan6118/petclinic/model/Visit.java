package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.exception.StatusInvalidException;
import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.model.constant.VisitType;
import com.github.farzan6118.petclinic.model.valueObject.DateTimeInterval;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Audited;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Embedded
    @Column(nullable = false)
    private DateTimeInterval dateTimeInterval;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitStatus status;

    @Column(length = 1000)
    private String description;

    private Instant bookedAt;


    public static Visit create(
            Vet vet,
            Pet pet,
            Room room,
            DateTimeInterval dateTimeInterval,
            VisitType visitType,
            String description
    ) {
        Visit visit = new Visit();

        visit.vet = vet;
        visit.pet = pet;
        visit.room = room;
        visit.dateTimeInterval = DateTimeInterval.of(date, startTime, endTime);
        visit.visitType = visitType;
        visit.description = description;
        visit.status = VisitStatus.SCHEDULED;
        visit.bookedAt = Instant.now();

        return visit;
    }

    public void reschedule(
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            Room room,
            String description
    ) {
        this.dateTimeInterval = DateTimeInterval.of(date, startTime, endTime);
        this.room = room;

        if (description != null) {
            this.description = description;
        }
    }

    public void cancel() {

        if (status == VisitStatus.COMPLETED) {
            throw new StatusInvalidException("Completed visit cannot be cancelled");
        }

        if (status == VisitStatus.CANCELLED) {
            return;
        }

        this.status = VisitStatus.CANCELLED;
    }

    public void complete() {

        if (status == VisitStatus.CANCELLED) {
            throw new StatusInvalidException("Cancelled visit cannot be completed");
        }

        if (status == VisitStatus.COMPLETED) {
            throw new StatusInvalidException("Visit is already completed");
        }

        this.dateTimeInterval = DateTimeInterval.of(this.dateTimeInterval.getStart(), LocalDateTime.now());
        this.status = VisitStatus.COMPLETED;
    }
}