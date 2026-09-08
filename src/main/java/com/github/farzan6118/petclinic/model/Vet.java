package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.model.valueObject.DateTimeInterval;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Audited;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@Audited
public class Vet extends BaseEntity<Long> {

    @Size(max = 10)
    private String title;

    @Size(max = 128)
    @Column(nullable = false)
    private String firstName;

    @Size(max = 128)
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true)
    private String nationalId;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @Email
    @NotBlank
    @Size(max = 128)
    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VetAvailability> availabilities = new ArrayList<>();

    public String getFullName() {
        return Stream.of(firstName, lastName)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(" "));
    }

    public void updateProfile(
            String city,
            String address,
            LocalDate birthDate,
            String specialty
    ) {
        if (profile == null) {
            updateProfile(new Profile());
        }
        profile.setCity(city);
        profile.setAddress(address);
        profile.setBirthDate(birthDate);
        profile.setSpecialty(specialty);
    }

    public void updateProfile(Profile profile) {
        this.profile = profile;
        profile.setVet(this);
    }

    public void addAvailability(VetAvailability availability) {
        availabilities.add(availability);
        availability.setVet(this);
    }

    public void removeAvailability(VetAvailability availability) {
        availabilities.remove(availability);
        availability.setVet(null);
    }

    public void addAvailability(
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        VetAvailability availability = new VetAvailability();
        availability.setDateTimeInterval(DateTimeInterval.of(date, startTime, endTime));
        addAvailability(availability);
    }
}
