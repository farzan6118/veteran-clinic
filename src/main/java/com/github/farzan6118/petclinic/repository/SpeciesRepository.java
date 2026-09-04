package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.Species;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpeciesRepository extends JpaRepository<Species, Integer> {

    Optional<Species> findByUuid(UUID uuid);

    boolean existsByCode(String code);

    boolean existsByCodeAndUuidNot(String code, UUID uuid);
}
