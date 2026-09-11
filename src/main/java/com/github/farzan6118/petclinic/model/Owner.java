package com.github.farzan6118.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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

    @Size(max = 10)
    private String title;

    @Size(max = 128)
    private String firstName;

    @Size(max = 128)
    private String lastName;

    @Size(max = 20)
    private String nationalId;

    @Past
    private LocalDate birthDate;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    private String city;
    private String address;

    public String getFullName() {
        return Stream.of(firstName, lastName)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(" "));
    }
}
