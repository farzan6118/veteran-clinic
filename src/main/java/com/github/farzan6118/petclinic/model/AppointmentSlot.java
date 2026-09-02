package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.model.constant.SlotStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Audited;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Audited
public class AppointmentSlot extends BaseEntity<Long> {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "vet_id",
            nullable = false
    )
    private Vet vet;


    @Column(nullable = false)
    private LocalDate date;


    @Column(nullable = false)
    private LocalTime startTime;


    @Column(nullable = false)
    private LocalTime endTime;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status = SlotStatus.AVAILABLE;


    @OneToOne(mappedBy = "appointmentSlot")
    private Visit visit;


    public void book(Visit visit) {

        if (this.status != SlotStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Appointment slot is not available"
            );
        }

        this.status = SlotStatus.BOOKED;
        this.visit = visit;
    }


    public void cancel() {

        if (this.status != SlotStatus.BOOKED) {
            return;
        }

        this.status = SlotStatus.AVAILABLE;
        this.visit = null;
    }


    public boolean isAvailable() {
        return status == SlotStatus.AVAILABLE;
    }
}