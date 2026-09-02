package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
public class Owner extends BaseEntity<Long> {
    private String firstname;
    private String lastname;
    private String nationalCode;
    private LocalDate birthDate;
    @Column(unique = true, nullable = false)
    private String telephone;
    @Email
    @Column(unique = true, nullable = false)
    private String email;
    private String city;
    private String address;

    public String getFullName() {
        return Stream.of(firstname, lastname)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(" "));
    }

}
