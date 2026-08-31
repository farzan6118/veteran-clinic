package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.model.constant.PetStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Pet extends BaseEntity<Long> {

    @Column(nullable = false)
    private String name;
    private LocalDate birthDate;
    private String color;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_type_id", nullable = false)
    private PetType petType;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Enumerated(EnumType.STRING)
    private PetStatus status;

    // todo: Add medical record entity manyToOne later
}
