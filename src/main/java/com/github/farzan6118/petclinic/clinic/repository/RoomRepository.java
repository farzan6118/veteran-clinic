package com.github.farzan6118.petclinic.clinic.repository;

import com.github.farzan6118.petclinic.clinic.model.Room;
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

    boolean existsByRoomNumber(String code);

    boolean existsByRoomNumberAndUuidNot(String roomNumber, UUID uuid);

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
