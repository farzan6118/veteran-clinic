package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Pet extends BaseEntity<Long> {

    private String name;
    private String code;
    private LocalDate birthDate;

    @OneToOne(optional = false)
    private PetType petType;

    @ManyToOne(optional = false)
    private Owner owner;

}
