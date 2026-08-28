package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.CreateVisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.service.VisitService;
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
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<VisitResponseDto> bookVisit(
            @Valid @RequestBody CreateVisitRequestDto request) {
        VisitResponseDto response = visitService.bookVisit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<VisitResponseDto>> getMyVisits() {
        return ResponseEntity.ok(visitService.getMyVisits());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<VisitResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(visitService.getByUuid(uuid));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> cancelVisit(@PathVariable UUID uuid) {
        visitService.cancelVisit(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vet")
    public ResponseEntity<List<VisitResponseDto>> getVetVisits() {
        return ResponseEntity.ok(visitService.getVetVisits());
    }

    @PatchMapping("/{uuid}/complete")
    public ResponseEntity<VisitResponseDto> completeVisit(
            @PathVariable UUID uuid,
            @Valid @RequestBody CompleteVisitRequest request) {
        return ResponseEntity.ok(
                visitService.completeVisit(uuid, request)
        );
    }
}

