package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Audited;

import java.time.Instant;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitStatus status;

    @Column(length = 1000)
    private String description;

    private Instant bookedAt;


    public static Visit create(Pet pet, Vet vet, AppointmentSlot slot, String description) {

        Visit visit = new Visit();

        visit.pet = pet;
        visit.vet = vet;
        visit.appointmentSlot = slot;
        visit.description = description;
        visit.status = VisitStatus.SCHEDULED;

        return visit;
    }


    public void cancel() {

        if (status == VisitStatus.COMPLETED) {
            throw new IllegalStateException("Completed visit cannot be cancelled");
        }

        this.status = VisitStatus.CANCELLED;
    }


    public void complete() {

        this.status = VisitStatus.COMPLETED;
    }
}