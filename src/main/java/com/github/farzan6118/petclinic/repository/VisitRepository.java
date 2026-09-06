package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.AppointmentSlot;
import com.github.farzan6118.petclinic.model.Room;
import com.github.farzan6118.petclinic.model.Visit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    boolean existsByVetUuidAndAppointmentSlot(UUID vetUuid, AppointmentSlot appointmentSlot);

    @EntityGraph(attributePaths = {
            "vet",
            "pet",
            "pet.owner",
            "pet.species",
    })
    Optional<Visit> findByUuid(UUID uuid);

    List<Visit> findAllByPetOwnerUuidOrderByAppointmentSlotDateAsc(UUID currentOwnerUuid);

    List<Visit> findAllByVetUuidOrderByAppointmentSlotDateAsc(UUID currentVetUuid);

    @Query("""
            select case when count(v) > 0 then true else false end
            from Visit v
            where v.vet.uuid = :vetUuid
              and v.status not in (com.github.farzan6118.petclinic.model.constant.VisitStatus.CANCELLED,
                       com.github.farzan6118.petclinic.model.constant.VisitStatus.NO_SHOW)
              and v.visitStart < :visitEnd
              and v.visitEnd > :visitStart
              and (:excludedVisitUuid is null or v.uuid <> :excludedVisitUuid)
            """)
    boolean existsVetReservation(
            @Param("vetUuid") UUID vetUuid,
            @Param("visitStart") LocalDateTime visitStart,
            @Param("visitEnd") LocalDateTime visitEnd,
            @Param("excludedVisitUuid") UUID excludedVisitUuid
    );

    @Query("""
            select case when count(v) > 0 then true else false end
            from Visit v
            where v.room = :room
              and v.status not in (com.github.farzan6118.petclinic.model.constant.VisitStatus.CANCELLED,
                                   com.github.farzan6118.petclinic.model.constant.VisitStatus.NO_SHOW)
              and v.visitStart < :visitEnd
              and v.visitEnd > :visitStart
              and (:excludedVisitUuid is null or v.uuid <> :excludedVisitUuid)
            """)
    boolean existsRoomReservation(
            @Param("room") Room room,
            @Param("visitStart") LocalDateTime visitStart,
            @Param("visitEnd") LocalDateTime visitEnd,
            @Param("excludedVisitUuid") UUID excludedVisitUuid
    );
}
