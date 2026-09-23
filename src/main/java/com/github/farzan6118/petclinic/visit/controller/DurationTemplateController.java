package com.github.farzan6118.petclinic.visit.controller;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.visit.dto.request.CreateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.UpdateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.visit.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.visit.service.DurationTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/duration-templates")
@RequiredArgsConstructor
public class DurationTemplateController {

    private final DurationTemplateService durationTemplateService;

    @GetMapping("/{uuid}")
    public DurationTemplateResponseDto getByUuid(@PathVariable UUID uuid) {
        return durationTemplateService.getByUuid(uuid);
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponseDto<DurationTemplateResponseDto>> findAllPageable(
            @ModelAttribute @Valid PageAndSortRequestDto requestDto) {
        return ResponseEntity.ok(durationTemplateService.findAllPageable(requestDto));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreateDurationTemplateRequestDto request) {
        durationTemplateService.create(request);
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateDurationTemplateRequestDto request
    ) {
        durationTemplateService.update(uuid, request);
    }

    @PatchMapping("/{uuid}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable UUID uuid) {
        durationTemplateService.activate(uuid);
    }

    @PatchMapping("/{uuid}/inactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactivate(@PathVariable UUID uuid) {
        durationTemplateService.inactivate(uuid);
    }

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuid) {
        durationTemplateService.delete(uuid);
    }
}