package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.VetAvailability;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VetRepository extends JpaRepository<Vet, Long> {

    Optional<Vet> findByUuid(UUID uuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Vet v where v.uuid = :uuid")
    Optional<Vet> findByUuidWithLock(@Param("uuid") UUID uuid);

    Optional<Vet> findByEmail(String email);

    Optional<Vet> findByMobileNumber(String mobile);

    boolean existsByEmailAndUuidNot(String email, UUID uuid);

    boolean existsByMobileNumberAndUuidNot(String mobile, UUID uuid);

    boolean existsByMobileNumber(String mobile);

    boolean existsByEmail(String email);

    boolean existsByUuid(UUID vetUuid);

    List<Vet> findByAvailabilities(List<VetAvailability> availabilities);

    @Query("""
            select va from VetAvailability va
                join fetch va.vet v
                where v.uuid = :uuid
                and (:startTime >= va.startTime
                and :endTime <= va.endTime)
            """
    )
    Optional<Vet> findAvailableByUuidAndTimeRange(
            UUID uuid, LocalDateTime startTime, LocalDateTime endTime);
}
