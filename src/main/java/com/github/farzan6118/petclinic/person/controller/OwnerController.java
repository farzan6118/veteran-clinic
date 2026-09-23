package com.github.farzan6118.petclinic.person.controller;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.person.dto.request.CreatePersonRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.UpdatePersonRequestDto;
import com.github.farzan6118.petclinic.person.dto.response.PersonResponseDto;
import com.github.farzan6118.petclinic.person.service.OwnerService;
import com.github.farzan6118.petclinic.pet.dto.response.PetResponseDto;
import com.github.farzan6118.petclinic.pet.service.PetService;
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
@RequestMapping("/api/persons")
public class OwnerController {

    private final OwnerService ownerService;
    private final PetService petService;

    @GetMapping("/{uuid}")
    public ResponseEntity<PersonResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ownerService.getByUuid(uuid));
    }

    @GetMapping("/{uuid}/pets")
    public ResponseEntity<List<PetResponseDto>> getPetListByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(petService.getPetListByOwnerUuid(uuid));
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponseDto<PersonResponseDto>> findAll(
            @ModelAttribute @Valid PageAndSortRequestDto requestDto) {
        return ResponseEntity.ok(ownerService.findAll(requestDto));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePersonRequestDto request) {
        ownerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdatePersonRequestDto request) {
        ownerService.update(uuid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        ownerService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
