package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByUuid(UUID uuid);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobile);

    boolean existsByEmailAndUuidNot(String email, UUID uuid);

    boolean existsByMobileNumberAndUuidNot(String telephone, UUID uuid);
}
