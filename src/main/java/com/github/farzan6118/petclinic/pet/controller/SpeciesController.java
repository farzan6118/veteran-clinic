package com.github.farzan6118.petclinic.pet.controller;

import com.github.farzan6118.petclinic.common.dto.request.PageRequestDto;
import com.github.farzan6118.petclinic.common.dto.request.SortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.pet.dto.request.CreateSpeciesRequestDto;
import com.github.farzan6118.petclinic.pet.dto.request.UpdateSpeciesRequestDto;
import com.github.farzan6118.petclinic.pet.dto.response.SpeciesResponseDto;
import com.github.farzan6118.petclinic.pet.service.SpeciesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<PageResponseDto<SpeciesResponseDto>> findAll(PageRequestDto page, SortRequestDto sort) {
        return ResponseEntity.ok(speciesService.findAll(page, sort));
    }

    @GetMapping
    public ResponseEntity<List<SpeciesResponseDto>> findAll() {
        return ResponseEntity.ok(speciesService.findAll());
    }


    private Pageable getPageable(PageRequestDto page, SortRequestDto sort) {
        return PageRequest.of(
                page.pageNumber(),
                page.pageSize(),
                Sort.by(sort.sortDirection(), sort.sortBy())
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