package com.github.farzan6118.petclinic.clinic.model;

import com.github.farzan6118.petclinic.common.persistence.BaseEntity;
import com.github.farzan6118.petclinic.person.model.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Building extends BaseEntity<Integer> {

    @OneToOne(cascade = CascadeType.ALL, optional = false, mappedBy = "building")
    private Address address;

    @Column(nullable = false)
    private boolean active;

}
