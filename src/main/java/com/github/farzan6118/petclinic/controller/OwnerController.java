package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.controller.dto.request.CreateOwnerRequestDto;
import com.github.farzan6118.petclinic.controller.dto.request.UpdateOwnerRequestDto;
import com.github.farzan6118.petclinic.controller.dto.response.OwnerResponseDto;
import com.github.farzan6118.petclinic.service.OwnerService;
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
@RequestMapping("/api/owner")
public class OwnerController {


    private final OwnerService ownerService;

    @GetMapping("/{uuid}")
    public ResponseEntity<OwnerResponseDto> getById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ownerService.getById(uuid));
    }

    @GetMapping
    public ResponseEntity<List<OwnerResponseDto>> findAll() {
        return ResponseEntity.ok(ownerService.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateOwnerRequestDto request) {
        ownerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<OwnerResponseDto> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateOwnerRequestDto request
    ) {
        return ResponseEntity.ok(ownerService.update(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        ownerService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
