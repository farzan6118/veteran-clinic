package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Profile extends BaseEntity<Long> {

    private String city;
    private String address;
    private String specialty;
    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vet_id",
            unique = true
    )
    private Vet vet;
}
