package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.VetAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VetAvailabilityRepository extends JpaRepository<VetAvailability, Long> {

    List<VetAvailability> findAllByVetUuidAndDateAndActiveTrue(UUID vetUuid, LocalDate date);

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM VetAvailability a
                WHERE a.vet.uuid = :vetUuid
                  AND a.date = :date
                  AND a.active = true
                  AND a.startTime < :endTime
                  AND a.endTime > :startTime
            """)
    boolean existsOverlappingAvailability(
            @Param("vetUuid") UUID vetUuid,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM VetAvailability a
                WHERE a.vet.uuid = :vetUuid
                  AND a.uuid <> :availabilityUuid
                  AND a.date = :date
                  AND a.active = true
                  AND a.startTime < :endTime
                  AND a.endTime > :startTime
            """)
    boolean existsOverlappingAvailabilityForUpdate(
            @Param("vetUuid") UUID vetUuid,
            @Param("availabilityUuid") UUID availabilityUuid,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    Optional<VetAvailability> findByUuidAndVetUuid(UUID uuid, UUID vetUuid);
}