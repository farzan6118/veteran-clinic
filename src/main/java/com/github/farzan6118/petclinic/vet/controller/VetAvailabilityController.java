package com.github.farzan6118.petclinic.vet.controller;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.vet.dto.request.CreateVetAvailabilityRequestDto;
import com.github.farzan6118.petclinic.vet.dto.request.UpdateVetAvailabilityRequestDto;
import com.github.farzan6118.petclinic.vet.dto.response.AvailabilityResponseDto;
import com.github.farzan6118.petclinic.vet.service.VetAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/page")
    public ResponseEntity<PageResponseDto<AvailabilityResponseDto>> getVetAvailabilityPageable(
            @PathVariable UUID vetUuid,
            @ModelAttribute @Valid PageAndSortRequestDto requestDto) {
        return ResponseEntity.ok(vetAvailabilityService.getVetAvailabilityPageable(vetUuid, requestDto));
    }

    @DeleteMapping("/{availabilityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvailability(@PathVariable UUID vetUuid, @PathVariable UUID availabilityUuid) {
        vetAvailabilityService.deleteAvailability(vetUuid, availabilityUuid);
    }
}