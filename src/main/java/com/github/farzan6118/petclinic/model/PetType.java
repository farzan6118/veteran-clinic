package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PetType extends BaseEntity<Integer> {

    private String name;
    private String description;

}
