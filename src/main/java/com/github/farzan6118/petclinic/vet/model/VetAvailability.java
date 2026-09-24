package com.github.farzan6118.petclinic.vet.model;

import com.github.farzan6118.petclinic.common.exception.BadRequestException;
import com.github.farzan6118.petclinic.common.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SQLRestriction("entity_status <> 'DELETED'")
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
            throw new BadRequestException("End time must be after start time");
        }

        if (Duration.between(startTime, endTime).toMinutes() < 2) {
            throw new BadRequestException("Availability duration must be at least 2 minutes");
        }

        if (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            throw new BadRequestException("Availability start and end must be on the same day");
        }
    }
}
