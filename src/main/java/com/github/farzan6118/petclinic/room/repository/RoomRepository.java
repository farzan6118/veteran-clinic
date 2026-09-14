package com.github.farzan6118.petclinic.room.repository;

import com.github.farzan6118.petclinic.room.model.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    Optional<Room> findByUuid(UUID uuid);

    boolean existsByCode(String code);

    boolean existsByCodeAndUuidNot(String code, UUID roomUuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select room
            from Room room
            where room.active = true
                and lower(room.roomType.name) in :roomTypeNames
            order by room.id
            """)
    List<Room> findActiveRoomsByTypeNames(@Param("roomTypeNames") List<String> roomTypeNames);
}
