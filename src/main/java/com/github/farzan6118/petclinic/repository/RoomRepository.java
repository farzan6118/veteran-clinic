package com.github.farzan6118.petclinic.repository;

import com.github.farzan6118.petclinic.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    Optional<Room> findByUuid(UUID uuid);

    boolean existsByCode(String code);

    boolean existsByCodeAndUuidNot(String code, UUID roomUuid);
}
