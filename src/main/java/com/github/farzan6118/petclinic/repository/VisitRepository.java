package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.Room;
import com.github.farzan6118.petclinic.model.Visit;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @EntityGraph(attributePaths = {
            "vet",
            "pet",
            "pet.owner",
            "pet.species",
            "room",
    })
    Optional<Visit> findByUuid(UUID uuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Visit v where v.uuid = :uuid")
    Optional<Visit> findByUuidForUpdate(@Param("uuid") UUID uuid);

    List<Visit> findAllByPetOwnerUuidOrderByDateAscStartTimeAsc(UUID currentOwnerUuid);

    List<Visit> findAllByVetUuidOrderByDateAscStartTimeAsc(UUID currentVetUuid);

    @Query("""
            select case when count(v) > 0 then true else false end
            from Visit v
            where v.vet.uuid = :vetUuid
              and v.status not in (com.github.farzan6118.petclinic.model.constant.VisitStatus.CANCELLED,
                       com.github.farzan6118.petclinic.model.constant.VisitStatus.NO_SHOW)
              and (v.date < :date or (v.date = :date and v.startTime < :endTime))
              and (v.date > :date or (v.date = :date and v.endTime > :startTime))
              and (:excludedVisitUuid is null or v.uuid <> :excludedVisitUuid)
            """)
    boolean existsVetReservation(
            @Param("vetUuid") UUID vetUuid,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludedVisitUuid") UUID excludedVisitUuid
    );

    @Query("""
            select case when count(v) > 0 then true else false end
            from Visit v
            where v.room = :room
              and v.status not in (com.github.farzan6118.petclinic.model.constant.VisitStatus.CANCELLED,
                                   com.github.farzan6118.petclinic.model.constant.VisitStatus.NO_SHOW)
              and (v.date < :date or (v.date = :date and v.startTime < :endTime))
              and (v.date > :date or (v.date = :date and v.endTime > :startTime))
              and (:excludedVisitUuid is null or v.uuid <> :excludedVisitUuid)
            """)
    boolean existsRoomReservation(
            @Param("room") Room room,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludedVisitUuid") UUID excludedVisitUuid
    );
}
