package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.AppointmentSlot;
import com.github.farzan6118.petclinic.model.Visit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    boolean existsByVetUuidAndAppointmentSlot(UUID vetUuid, AppointmentSlot appointmentSlot);

    @EntityGraph(attributePaths = {
            "vet",
            "pet",
            "pet.owner",
            "pet.petType",
    })
    Optional<Visit> findByUuid(UUID uuid);

    List<Visit> findAllByPetOwnerUuidOrderByAppointmentSlotDateAsc(UUID currentOwnerUuid);

    List<Visit> findAllByVetUuidOrderByAppointmentSlotDateAsc(UUID currentVetUuid);
}
