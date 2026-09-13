package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CreateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.service.DurationTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public List<DurationTemplateResponseDto> findAll() {
        return durationTemplateService.findAll();
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
}