package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.DurationTemplate;
import com.github.farzan6118.petclinic.model.constant.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
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

    List<DurationTemplate> findAllByEntityStatus(EntityStatus entityStatus);
}
