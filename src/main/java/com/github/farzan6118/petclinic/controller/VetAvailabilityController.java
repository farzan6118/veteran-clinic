package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CreateVetAvailabilityRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetAvailabilityRequestDto;
import com.github.farzan6118.petclinic.dto.response.AvailabilityResponseDto;
import com.github.farzan6118.petclinic.service.VetAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vets/{vetUuid}/availabilities")
@Validated
public class VetAvailabilityController {

    private final VetAvailabilityService vetAvailabilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createAvailability(
            @PathVariable UUID vetUuid, @Valid @RequestBody CreateVetAvailabilityRequestDto request) {
        vetAvailabilityService.createAvailability(vetUuid, request);
    }

    @PutMapping("/{availabilityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAvailability(
            @PathVariable UUID vetUuid,
            @PathVariable UUID availabilityUuid,
            @Valid @RequestBody UpdateVetAvailabilityRequestDto request
    ) {
        vetAvailabilityService.updateAvailability(vetUuid, availabilityUuid, request);
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityResponseDto>> getVetAvailability(@PathVariable UUID vetUuid) {
        return ResponseEntity.ok(vetAvailabilityService.getVetAvailability(vetUuid));
    }

    @DeleteMapping("/{availabilityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvailability(@PathVariable UUID vetUuid, @PathVariable UUID availabilityUuid) {
        vetAvailabilityService.deleteAvailability(vetUuid, availabilityUuid);
    }
}