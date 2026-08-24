package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.controller.dto.request.CreatePetTypeRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdatePetTypeRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.PetTypeResponseDto;
import com.github.farzan6118.petclinic.service.PetTypeService;
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
@RequestMapping("/api/pet-types")
public class PetTypeController {

    private final PetTypeService petTypeService;

    @GetMapping("/{uuid}")
    public ResponseEntity<PetTypeResponseDto> getById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(petTypeService.getById(uuid));
    }

    @GetMapping
    public ResponseEntity<List<PetTypeResponseDto>> findAll() {
        return ResponseEntity.ok(petTypeService.findAll());
    }

    @PostMapping
    public ResponseEntity<PetTypeResponseDto> create(@Valid @RequestBody CreatePetTypeRequestDto request) {
        PetTypeResponseDto response = petTypeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PetTypeResponseDto> update(@PathVariable UUID uuid,
                                                     @Valid @RequestBody UpdatePetTypeRequestDto request) {
        return ResponseEntity.ok(petTypeService.update(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        petTypeService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}