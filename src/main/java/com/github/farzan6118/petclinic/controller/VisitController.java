package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.dto.request.VisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetAvailableSlotResponseDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    @GetMapping("/vets/{vetUuid}/available-slots")
    public ResponseEntity<List<VetAvailableSlotResponseDto>> getAvailableSlots(
            @PathVariable UUID vetUuid, @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(visitService.getAvailableSlots(vetUuid, date));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void bookVisit(@Valid @RequestBody VisitRequestDto request) {
        visitService.bookVisit(request);
    }

    @PutMapping("{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    public void rescheduleVisit(
            @PathVariable UUID uuid, @Valid @RequestBody RescheduleVisitRequestDto request) {
        visitService.rescheduleVisit(uuid, request);
    }

    @GetMapping
    public ResponseEntity<List<VisitResponseDto>> getAllVisits() {
        return ResponseEntity.ok(visitService.getAllVisits());
    }

    @GetMapping("/my")
    public ResponseEntity<List<VisitResponseDto>> getMyVisits(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(visitService.getMyVisits(jwt));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<VisitResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(visitService.getByUuid(uuid));
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelVisit(@PathVariable UUID uuid, String reason) {
        visitService.cancelVisit(uuid, reason);
    }

    @GetMapping("/vet")
    public ResponseEntity<List<VisitResponseDto>> getVetVisits() {
        return ResponseEntity.ok(visitService.getVetVisits());
    }

    @PatchMapping("/{uuid}/complete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void completeVisit(
            @PathVariable UUID uuid,
            @Valid @RequestBody CompleteVisitRequest request) {
        visitService.completeVisit(uuid, request);
    }
}

