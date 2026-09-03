package com.github.farzan6118.petclinic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class VetAvailability extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean active = true;

    public static VetAvailability create(
            Vet vet,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        VetAvailability availability = new VetAvailability();

        availability.vet = vet;
        availability.date = date;
        availability.startTime = startTime;
        availability.endTime = endTime;
        availability.active = true;

        return availability;
    }

    public void updateSchedule(
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
