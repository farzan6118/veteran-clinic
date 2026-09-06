package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.exception.StatusInvalidException;
import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.model.constant.VisitType;
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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_slot_id", nullable = false, unique = true)
    private AppointmentSlot appointmentSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitType visitType;

    @Column(nullable = false)
    private LocalDateTime visitStart;

    @Column(nullable = false)
    private LocalDateTime visitEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitStatus status;

    @Column(length = 1000)
    private String description;

    private Instant bookedAt;


    public static Visit create(
            Pet pet,
            Vet vet,
            AppointmentSlot slot,
            Room room,
            VisitType visitType,
            LocalDateTime visitStart,
            LocalDateTime visitEnd,
            String description
    ) {
        Visit visit = new Visit();

        visit.pet = pet;
        visit.vet = vet;
        visit.appointmentSlot = slot;
        visit.room = room;
        visit.visitType = visitType;
        visit.visitStart = visitStart;
        visit.visitEnd = visitEnd;
        visit.description = description;
        visit.status = VisitStatus.SCHEDULED;
        visit.bookedAt = Instant.now();

        return visit;
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

        this.status = VisitStatus.COMPLETED;
    }
}