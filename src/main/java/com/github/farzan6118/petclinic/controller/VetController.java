package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.VetProfileUpdateRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetProfileResponseDto;
import com.github.farzan6118.petclinic.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.service.VetService;
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
@RequestMapping("/api/vets")
public class VetController {

    private final VetService vetService;

    @GetMapping("/{uuid}")
    public ResponseEntity<VetResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(vetService.getByUuid(uuid));
    }

    @GetMapping
    public ResponseEntity<List<VetResponseDto>> findAll() {
        return ResponseEntity.ok(vetService.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreateVetRequestDto request) {
        vetService.create(request);
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateVetRequestDto request) {
        vetService.update(uuid, request);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable UUID uuid) {
        vetService.delete(uuid);
    }

    @PutMapping("/{uuid}/profile")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateVetProfile(
            @Valid @RequestBody VetProfileUpdateRequestDto request,
            @PathVariable UUID uuid) {
        vetService.updateVetProfileByUuid(request, uuid);
    }

    @GetMapping("/{uuid}/profile")
    public ResponseEntity<VetProfileResponseDto> getVetProfile(@PathVariable UUID uuid) {
        return ResponseEntity.ok(vetService.getVetProfileByUuid(uuid));
    }

}

