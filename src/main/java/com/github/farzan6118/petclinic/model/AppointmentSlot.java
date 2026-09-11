package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.model.constant.AppointmentDuration;
import com.github.farzan6118.petclinic.model.constant.AppointmentType;
import com.github.farzan6118.petclinic.model.constant.SlotStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Audited;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Audited
@Table(
        indexes = {
                @Index(
                        name = "idx_appointment_slot_vet_date",
                        columnList = "vet_id"
                ),
                @Index(
                        name = "idx_appointment_slot_room_date",
                        columnList = "room_id"
                ),
                @Index(
                        name = "idx_appointment_slot_status",
                        columnList = "status"
                )
        }
)
public class AppointmentSlot extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentDuration appointmentDuration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status = SlotStatus.AVAILABLE;


    public void book() {

        if (status != SlotStatus.AVAILABLE) {
            throw new IllegalStateException("Appointment slot is not available");
        }

        this.status = SlotStatus.BOOKED;
    }


    public void release() {

        if (status != SlotStatus.BOOKED) {
            throw new IllegalStateException("Only booked slots can be released");
        }

        this.status = SlotStatus.AVAILABLE;
    }


    public void block() {

        if (status == SlotStatus.BOOKED) {
            throw new IllegalStateException("Booked appointment slot cannot be blocked");
        }

        this.status = SlotStatus.BLOCKED;
    }


    public boolean isAvailable() {
        return status == SlotStatus.AVAILABLE;
    }
}