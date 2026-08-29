package com.github.farzan6118.petclinic.repository.jpa;

import com.github.farzan6118.petclinic.model.Pet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @EntityGraph(attributePaths = {
            "petType",
            "owner"
    })
    Optional<Pet> findByUuid(UUID uuid);
}
