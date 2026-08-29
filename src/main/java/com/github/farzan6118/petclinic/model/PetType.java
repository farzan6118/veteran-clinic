package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PetType extends BaseEntity<Integer> {

    @Column(nullable = false)
    private String name;
    @Column(unique = true)
    private String code;
    private String breed;
    private String origin;
    private String description;

}
