package com.github.farzan6118.petclinic.common.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Profile extends BaseEntity<Long> {

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Past
    private LocalDate birthDate;

    private String photo;

}
