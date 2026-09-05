package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CreateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateRoomTypeRequestDto;
import com.github.farzan6118.petclinic.dto.response.RoomTypeResponseDto;
import com.github.farzan6118.petclinic.service.RoomTypeService;
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
@RequestMapping("/api/room-type")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping("/{uuid}")
    public ResponseEntity<RoomTypeResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(roomTypeService.getByUuid(uuid));
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeResponseDto>> findAll() {
        return ResponseEntity.ok(roomTypeService.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateRoomTypeRequestDto request) {
        roomTypeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateRoomTypeRequestDto request) {
        roomTypeService.update(uuid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        roomTypeService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}

