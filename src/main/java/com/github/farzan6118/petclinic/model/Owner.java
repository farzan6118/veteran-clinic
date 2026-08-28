package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Audited;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Audited
public class Owner extends BaseEntity<Long> {
    private String firstname;
    private String lastname;
    private String nationalCode;
    private LocalDate birthDate;
    @Column(unique = true, nullable = false)
    private String telephone;
    private String email;
    private String city;
    private String address;

}
