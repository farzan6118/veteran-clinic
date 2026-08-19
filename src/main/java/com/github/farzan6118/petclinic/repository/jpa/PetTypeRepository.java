package com.github.farzan6118.petclinic.repository.jpa;

import com.github.farzan6118.petclinic.model.constant.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

// todo: later remove PetType from enum and make an entity for it
public interface PetTypeRepository extends JpaRepository<PetType, Long> {
}
