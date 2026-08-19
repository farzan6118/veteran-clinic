package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Owner extends BaseEntity<Long> {
    private UUID keycloakUserId;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;

}
