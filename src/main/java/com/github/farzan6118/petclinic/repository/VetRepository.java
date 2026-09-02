package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VetRepository extends JpaRepository<Vet, Long> {

    Optional<Vet> findByUuid(UUID uuid);

    Optional<Vet> findById(Long id);

    Optional<Vet> findByEmail(String email);

    Optional<Vet> findByTelephone(String telephone);

    boolean existsByEmailAndUuidNot(String email, UUID uuid);

    boolean existsByTelephoneAndUuidNot(String telephone, UUID uuid);

    boolean existsByTelephone(String telephone);

    boolean existsByEmail(String email);

}
