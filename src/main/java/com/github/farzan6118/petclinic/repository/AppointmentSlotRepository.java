package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.AppointmentSlot;
import com.github.farzan6118.petclinic.model.constant.SlotStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {

    List<AppointmentSlot> findAllByVetUuidAndDateAndStatus(
            UUID vetUuid,
            LocalDate date,
            SlotStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                select s
                from AppointmentSlot s
                where s.uuid = :uuid
                and s.status = 'AVAILABLE'
            """)
    Optional<AppointmentSlot> findAvailableSlotForUpdate(
            @Param("uuid") UUID uuid
    );

}