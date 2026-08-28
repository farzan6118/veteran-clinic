package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CreatePetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdatePetRequestDto;
import com.github.farzan6118.petclinic.dto.response.PetResponseDto;
import com.github.farzan6118.petclinic.service.PetService;
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
@RequestMapping("/api/pet")
public class PetController {

    private final PetService petService;

    @GetMapping("/{uuid}")
    public ResponseEntity<PetResponseDto> getById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(petService.getById(uuid));
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDto>> findAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePetRequestDto request) {
        petService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdatePetRequestDto request
    ) {
        petService.update(uuid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        petService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}

