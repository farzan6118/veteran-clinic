package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.VetAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VetAvailabilityRepository extends JpaRepository<VetAvailability, Long> {

    @Query("""
            select case when count(a) > 0 then true else false end
            from VetAvailability a
            where a.vet.uuid = :vetUuid
              and a.startTime <= :startTime
              and a.endTime >= :endTime
              and a.active = true
            """)
    boolean existsCoveringTime(
            @Param("vetUuid") UUID vetUuid,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    List<VetAvailability> findAllByVetUuidAndActiveTrue(UUID vetUuid, LocalDate date);

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM VetAvailability a
                WHERE a.vet.uuid = :vetUuid
                  AND a.endTime > :startTime
                  AND a.startTime < :endTime
                  AND a.active = true
            """)
    boolean existsOverlappingAvailability(
            @Param("vetUuid") UUID vetUuid,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM VetAvailability a
                WHERE a.vet.uuid = :vetUuid
                  AND a.uuid <> :availabilityUuid
                  AND a.endTime > :startTime
                  AND a.startTime < :endTime
                  AND a.active = true
            """)
    boolean existsOverlappingAvailabilityForUpdate(
            @Param("vetUuid") UUID vetUuid,
            @Param("availabilityUuid") UUID availabilityUuid,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    Optional<VetAvailability> findByUuidAndVetUuid(UUID uuid, UUID vetUuid);

    List<VetAvailability> findAllByVetUuid(UUID vetUuid);
}