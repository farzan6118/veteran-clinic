package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.model.constant.PetType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pet extends BaseEntity<Long> {

    private String name;
    private String birthDate;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @ManyToOne
    private Owner owner;

}
