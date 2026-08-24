package com.github.farzan6118.petclinic.repository.jpa;

import com.github.farzan6118.petclinic.model.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VetRepository extends JpaRepository<Vet, Long> {

    Optional<Vet> findByUuid(UUID uuid);
}
