package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.Pet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @EntityGraph(attributePaths = {
            "petType",
            "owner"
    })
    Optional<Pet> findByUuid(UUID uuid);

    @EntityGraph(attributePaths = {
            "petType"
    })
    List<Pet> findByOwnerUuid(UUID ownerUuid);
}
