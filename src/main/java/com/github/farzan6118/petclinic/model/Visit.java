package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Visit extends BaseEntity<Long> {

    @ManyToOne(optional = false)
    private Pet pet;

    @ManyToOne(optional = false)
    private Vet vet;

    private LocalDateTime visitDateTime;
    private String description;
    private String diagnosis;
    private String notes;
    private VisitStatus status;
}
