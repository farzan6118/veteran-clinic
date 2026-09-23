package com.github.farzan6118.petclinic.vet.controller;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.vet.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.vet.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.vet.dto.request.VetProfileUpdateRequestDto;
import com.github.farzan6118.petclinic.vet.dto.response.VetProfileResponseDto;
import com.github.farzan6118.petclinic.vet.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.vet.service.VetService;
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
@RequestMapping("/api/vets")
public class VetController {

    private final VetService vetService;

    @GetMapping("/{uuid}")
    public ResponseEntity<VetResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(vetService.getByUuid(uuid));
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponseDto<VetResponseDto>> findAllPageable(
            @ModelAttribute @Valid PageAndSortRequestDto requestDto) {
        return ResponseEntity.ok(vetService.findAllPageable(requestDto));
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

