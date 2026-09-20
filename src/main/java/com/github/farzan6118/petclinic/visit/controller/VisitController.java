package com.github.farzan6118.petclinic.visit.controller;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.visit.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.CreateVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.VisitAdvancedSearch;
import com.github.farzan6118.petclinic.visit.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.visit.service.VisitService;
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
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void bookVisit(@Valid @RequestBody CreateVisitRequestDto request) {
        visitService.bookVisit(request);
    }

    @PutMapping("{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    public void rescheduleVisit(
            @PathVariable UUID uuid, @Valid @RequestBody RescheduleVisitRequestDto request) {
        visitService.rescheduleVisit(uuid, request);
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponseDto<VisitResponseDto>> findAll(
            @ModelAttribute @Valid PageAndSortRequestDto requestDto) {
        return ResponseEntity.ok(visitService.findAll(requestDto));
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

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<VisitResponseDto>> advancedSearch(
            @ModelAttribute("request") @Valid VisitAdvancedSearch request) {
        return ResponseEntity.ok(visitService.advancedSearch(request));
    }

    @PatchMapping("/{uuid}/complete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void completeVisit(
            @PathVariable UUID uuid, @Valid @RequestBody CompleteVisitRequestDto request) {
        visitService.completeVisit(uuid, request);
    }
}

