package com.github.farzan6118.petclinic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class VetAvailability extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Vet vet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private Integer durationMinutes;

    @Column(nullable = false)
    private boolean active = true;

    public void updateSchedule(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            Integer durationMinutes
    ) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
    }

    public VetAvailability create(
            Vet vet,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            Integer durationMinutes
    ) {
        VetAvailability availability = new VetAvailability();

        availability.vet = vet;
        availability.dayOfWeek = dayOfWeek;
        availability.startTime = startTime;
        availability.endTime = endTime;
        availability.durationMinutes = durationMinutes;
        availability.active = true;

        return availability;
    }

}
