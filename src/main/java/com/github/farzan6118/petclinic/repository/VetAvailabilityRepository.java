package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.VetAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VetAvailabilityRepository extends JpaRepository<VetAvailability, Long> {

    Optional<VetAvailability> findByIdAndVetUuid(Long id, UUID vetUuid);

    List<VetAvailability> findAllByVetUuidAndActiveTrue(UUID vetUuid);

    List<VetAvailability> findAllByVetUuidAndDayOfWeekAndActiveTrue(UUID vetUuid, DayOfWeek dayOfWeek);

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM VetAvailability a
                WHERE a.vet.uuid = :vetUuid
                  AND a.dayOfWeek = :dayOfWeek
                  AND a.active = true
                  AND a.startTime < :endTime
                  AND a.endTime > :startTime
            """)
    boolean existsOverlappingAvailability(
            @Param("vetUuid") UUID vetUuid,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM VetAvailability a
                WHERE a.vet.uuid = :vetUuid
                  AND a.id <> :availabilityId
                  AND a.dayOfWeek = :dayOfWeek
                  AND a.active = true
                  AND a.startTime < :endTime
                  AND a.endTime > :startTime
            """)
    boolean existsOverlappingAvailabilityForUpdate(
            @Param("vetUuid") UUID vetUuid,
            @Param("availabilityId") Long availabilityId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}