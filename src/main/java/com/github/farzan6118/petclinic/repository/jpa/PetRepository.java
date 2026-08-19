package com.github.farzan6118.petclinic.repository.jpa;

import com.github.farzan6118.petclinic.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
