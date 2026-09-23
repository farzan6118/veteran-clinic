package com.github.farzan6118.petclinic.visit.controller;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.visit.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.CreateVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.VisitAdvancedSearch;
import com.github.farzan6118.petclinic.visit.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.visit.service.VisitServiceCommand;
import com.github.farzan6118.petclinic.visit.service.VisitServiceQuery;
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

    private final VisitServiceCommand visitServiceCommand;
    private final VisitServiceQuery visitServiceQuery;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void bookVisit(@Valid @RequestBody CreateVisitRequestDto request) {
        visitServiceCommand.bookVisit(request);
    }

    @PutMapping("{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    public void rescheduleVisit(
            @PathVariable UUID uuid, @Valid @RequestBody RescheduleVisitRequestDto request) {
        visitServiceCommand.rescheduleVisit(uuid, request);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelVisit(@PathVariable UUID uuid, String reason) {
        visitServiceCommand.cancelVisit(uuid, reason);
    }

    @PatchMapping("/{uuid}/complete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void completeVisit(
            @PathVariable UUID uuid, @Valid @RequestBody CompleteVisitRequestDto request) {
        visitServiceCommand.completeVisit(uuid, request);
    }


    @GetMapping("/page")
    public ResponseEntity<PageResponseDto<VisitResponseDto>> findAll(
            @ModelAttribute @Valid PageAndSortRequestDto requestDto) {
        return ResponseEntity.ok(visitServiceQuery.findAll(requestDto));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<VisitResponseDto> findByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(visitServiceQuery.findByUuid(uuid));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<VisitResponseDto>> advancedSearch(
            @ModelAttribute("request") @Valid VisitAdvancedSearch request) {
        return ResponseEntity.ok(visitServiceQuery.advancedSearch(request));
    }
}

