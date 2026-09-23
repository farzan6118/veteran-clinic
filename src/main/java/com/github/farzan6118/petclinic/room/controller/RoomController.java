package com.github.farzan6118.petclinic.room.controller;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.room.dto.request.CreateRoomRequestDto;
import com.github.farzan6118.petclinic.room.dto.request.UpdateRoomRequestDto;
import com.github.farzan6118.petclinic.room.dto.response.RoomResponseDto;
import com.github.farzan6118.petclinic.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{uuid}")
    public ResponseEntity<RoomResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(roomService.getByUuid(uuid));
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponseDto<RoomResponseDto>> findAll(
            @ModelAttribute @Valid PageAndSortRequestDto requestDto) {
        return ResponseEntity.ok(roomService.findAll(requestDto));
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

