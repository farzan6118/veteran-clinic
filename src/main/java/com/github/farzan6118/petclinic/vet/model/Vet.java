package com.github.farzan6118.petclinic.vet.model;

import com.github.farzan6118.petclinic.common.persistence.BaseEntity;
import com.github.farzan6118.petclinic.person.model.Person;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Audited
public class Vet extends BaseEntity<Long> {

    @Size(max = 10)
    private String title;

    @OneToOne(optional = false)
    private Person person;

    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VetAvailability> availabilities = new ArrayList<>();

    public void addAvailability(VetAvailability availability) {
        availabilities.add(availability);
        availability.setVet(this);
    }

    public void removeAvailability(VetAvailability availability) {
        availabilities.remove(availability);
        availability.setVet(null);
    }

    public void addAvailability(LocalDate date, LocalTime startTime, LocalTime endTime) {
        VetAvailability availability = new VetAvailability();
        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
        availability.setStartTime(startDateTime);
        availability.setEndTime(endDateTime);
        addAvailability(availability);
    }
}
