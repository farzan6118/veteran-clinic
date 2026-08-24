package com.github.farzan6118.petclinic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Pet extends BaseEntity<Long> {

    private String name;
    private LocalDate birthDate;

    @OneToOne(optional = false)
    private PetType petType;

    @ManyToOne
    private Owner owner;

}
