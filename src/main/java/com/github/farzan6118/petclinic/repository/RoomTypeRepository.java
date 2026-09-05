package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    Optional<RoomType> findByUuid(UUID uuid);

}
