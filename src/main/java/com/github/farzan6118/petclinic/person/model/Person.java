package com.github.farzan6118.petclinic.person.model;

import com.github.farzan6118.petclinic.common.persistence.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
public class Person extends BaseEntity<Long> {

    @Size(max = 10)
    private String title;

    @Size(max = 128)
    private String firstName;

    @Size(max = 128)
    private String lastName;

    @Size(max = 20)
    private String nationalId;

    @OneToOne(cascade = CascadeType.ALL, optional = false, mappedBy = "owner")
    private Profile profile;

    @OneToOne(cascade = CascadeType.ALL, optional = false, mappedBy = "owner")
    private Address address;

    public String getFullName() {
        return Stream.of(firstName, lastName)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(" "));
    }


}
