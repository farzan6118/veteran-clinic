package com.github.farzan6118.petclinic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class VetAvailability extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private boolean active = true;


    public VetAvailability create(Vet vet, LocalDateTime startTime, LocalDateTime endTime) {
        dateAndTimeValidations(startTime, endTime);
        VetAvailability availability = new VetAvailability();
        availability.vet = vet;
        availability.startTime = startTime;
        availability.endTime = endTime;
        availability.active = true;
        return availability;
    }

    public void update(LocalDateTime startTime, LocalDateTime endTime) {
        dateAndTimeValidations(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void dateAndTimeValidations(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        if (Duration.between(startTime, endTime).toMinutes() < 5) {
            throw new IllegalArgumentException("duration cannot be less than 5 minutes");
        }

        if (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            throw new IllegalArgumentException("the start and end time must be the same day");
        }
    }
}
