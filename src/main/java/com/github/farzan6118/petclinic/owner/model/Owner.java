package com.github.farzan6118.petclinic.owner.model;

import com.github.farzan6118.petclinic.common.persistence.BaseEntity;
import com.github.farzan6118.petclinic.person.model.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Owner extends BaseEntity<Long> {

    @OneToOne(optional = false)
    private Person person;

}
