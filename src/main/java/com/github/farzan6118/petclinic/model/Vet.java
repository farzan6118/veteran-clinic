package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Vet extends BaseEntity<Long> {
    private String firstname;
    private String lastname;
    private String specialty;
    private String nationalCode;
    private LocalDate birthDate;
    @Column(unique = true, nullable = false)
    private String telephone;
    private String address;
    private String email;
    private String city;
}
