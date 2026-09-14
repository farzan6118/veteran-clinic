package com.github.farzan6118.petclinic.room.repository;

import com.github.farzan6118.petclinic.room.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    Optional<RoomType> findByUuid(UUID uuid);

}
