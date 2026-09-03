package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.VetProfileUpdateRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetAvailableSlotResponseDto;
import com.github.farzan6118.petclinic.dto.response.VetProfileResponseDto;
import com.github.farzan6118.petclinic.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.service.VetService;
import com.github.farzan6118.petclinic.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vets")
public class VetController {

    private final VetService vetService;
    private final VisitService visitService;

    @GetMapping("/{uuid}")
    public ResponseEntity<VetResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(vetService.getByUuid(uuid));
    }

    @GetMapping
    public ResponseEntity<List<VetResponseDto>> findAll() {
        return ResponseEntity.ok(vetService.findAll());
    }

    @PostMapping
    public ResponseEntity<VetResponseDto> create(@Valid @RequestBody CreateVetRequestDto request) {
        vetService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<VetResponseDto> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateVetRequestDto request) {
        vetService.update(uuid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        vetService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}/profile")
    public ResponseEntity<VetProfileResponseDto> updateVetProfile(
            @Valid @RequestBody VetProfileUpdateRequestDto request,
            @PathVariable UUID uuid) {
        vetService.updateVetProfileByUuid(request, uuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{uuid}/profile")
    public ResponseEntity<VetProfileResponseDto> getVetProfile(@PathVariable UUID uuid) {
        return ResponseEntity.ok(vetService.getVetProfileByUuid(uuid));
    }

    @GetMapping("/{vetUuid}/available-slots")
    public List<VetAvailableSlotResponseDto> getAvailableSlots(
            @PathVariable UUID vetUuid,
            @RequestParam LocalDate date
    ) {
        return visitService.getAvailableSlots(
                vetUuid,
                date
        );
    }

}

