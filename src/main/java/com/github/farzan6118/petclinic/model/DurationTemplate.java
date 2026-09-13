package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
public class DurationTemplate extends BaseEntity<Integer> {

    @Length(max = 32)
    @Column(nullable = false, unique = true)
    private String name;

    @PositiveOrZero
    @Column(nullable = false, unique = true)
    private Integer durationMinutes;

    @Length(max = 64)
    private String description;
}