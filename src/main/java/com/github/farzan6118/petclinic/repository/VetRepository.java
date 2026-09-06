package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.Vet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VetRepository extends JpaRepository<Vet, Long> {

    Optional<Vet> findByUuid(UUID uuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Vet v where v.uuid = :uuid")
    Optional<Vet> findByUuidForUpdate(@Param("uuid") UUID uuid);

    Optional<Vet> findByEmail(String email);

    Optional<Vet> findByMobileNumber(String mobile);

    boolean existsByEmailAndUuidNot(String email, UUID uuid);

    boolean existsByMobileNumberAndUuidNot(String mobile, UUID uuid);

    boolean existsByMobileNumber(String mobile);

    boolean existsByEmail(String email);

    boolean existsByUuid(UUID vetUuid);
}
