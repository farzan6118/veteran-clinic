package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CreateSpeciesRequestDto;
import com.github.farzan6118.petclinic.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateSpeciesRequestDto;
import com.github.farzan6118.petclinic.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.dto.response.SpeciesResponseDto;
import com.github.farzan6118.petclinic.service.SpeciesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/species")
public class SpeciesController {

    private final SpeciesService speciesService;

    @GetMapping("/{uuid}")
    public ResponseEntity<SpeciesResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(speciesService.getByUuid(uuid));
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponseDto<SpeciesResponseDto>> findAll(PageAndSortRequestDto pageable) {
        return ResponseEntity.ok(speciesService.findAll(pageable));
    }

    @GetMapping
    public ResponseEntity<List<SpeciesResponseDto>> findAll() {
        return ResponseEntity.ok(speciesService.findAll());
    }


    private Pageable getPageable(PageAndSortRequestDto request) {
        return PageRequest.of(
                request.pageNumber(),
                request.pageSize(),
                Sort.by(request.sortDirection(), request.sortBy())
        );
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateSpeciesRequestDto request) {
        speciesService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> update(@PathVariable UUID uuid,
                                       @Valid @RequestBody UpdateSpeciesRequestDto request) {
        speciesService.update(uuid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        speciesService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}