package com.github.farzan6118.petclinic.visit.repository;

import com.github.farzan6118.petclinic.common.enums.EntityStatus;
import com.github.farzan6118.petclinic.visit.model.DurationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DurationTemplateRepository extends JpaRepository<DurationTemplate, Integer> {

    Optional<DurationTemplate> findByUuid(UUID uuid);

    Optional<DurationTemplate> findByUuidAndEntityStatus(UUID uuid, EntityStatus entityStatus);

    Optional<DurationTemplate> findByNameIgnoreCase(String name);

    Optional<DurationTemplate> findByDurationMinutes(Integer durationMinutes);

    boolean existsByDurationMinutes(Integer durationMinutes);

    boolean existsByName(String name);

    boolean existsByNameAndUuidNot(String name, UUID uuid);

    boolean existsByDurationMinutesAndUuidNot(Integer duration, UUID uuid);

    Page<DurationTemplate> findAllByEntityStatus(EntityStatus entityStatus, Pageable pageable);
}
