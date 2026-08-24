package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PetType extends BaseEntity<Integer> {

    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true)
    private String code;
    private String description;

}
