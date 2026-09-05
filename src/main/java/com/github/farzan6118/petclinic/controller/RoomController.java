package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CreateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateRoomRequestDto;
import com.github.farzan6118.petclinic.dto.response.RoomResponseDto;
import com.github.farzan6118.petclinic.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{uuid}")
    public ResponseEntity<RoomResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(roomService.getByUuid(uuid));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> findAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateRoomRequestDto request) {
        roomService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateRoomRequestDto request) {
        roomService.update(uuid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        roomService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}

