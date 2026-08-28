package com.github.farzan6118.petclinic.repository.jpa;

import com.github.farzan6118.petclinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    boolean existsByVetUuidAndVisitDateTime(UUID uuid, LocalDateTime localDateTime);

    Optional<Visit> findByUuid(UUID uuid);

    List<Visit> findAllByVetUuidOrderByVisitDateTimeAsc(UUID currentVetUuid);

    List<Visit> findAllByPetOwnerUuidOrderByVisitDateTimeDesc(UUID currentUserUuid);
}
