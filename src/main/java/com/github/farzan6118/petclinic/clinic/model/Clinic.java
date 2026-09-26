package com.github.farzan6118.petclinic.clinic.model;

import com.github.farzan6118.petclinic.common.persistence.BaseEntity;
import com.github.farzan6118.petclinic.person.model.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("entity_status <> 'DELETED'")
public class Clinic extends BaseEntity<Integer> {

    @OneToOne(
            cascade = CascadeType.ALL,
            optional = false,
            orphanRemoval = true
    )
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    private Address address;

    @Column(nullable = false)
    private boolean active;

}
