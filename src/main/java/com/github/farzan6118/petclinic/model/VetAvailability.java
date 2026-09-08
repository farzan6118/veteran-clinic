package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.model.valueObject.DateTimeInterval;
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

    @Embedded
    @Column(nullable = false)
    private DateTimeInterval dateTimeInterval;

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
        availability.dateTimeInterval = DateTimeInterval.of(date, startTime, endTime);
        availability.active = true;

        return availability;
    }

    public void updateSchedule(
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        this.dateTimeInterval = DateTimeInterval.of(date, startTime, endTime);
    }

}
