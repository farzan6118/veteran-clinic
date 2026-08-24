package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.controller.dto.request.CreatePetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdatePetRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.PetResponseDto;
import com.github.farzan6118.petclinic.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<PetResponseDto> create(@Valid @RequestBody CreatePetRequestDto request) {
        return ResponseEntity.ok(petService.create(request));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PetResponseDto> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdatePetRequestDto request
    ) {
        return ResponseEntity.ok(petService.update(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        petService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}

