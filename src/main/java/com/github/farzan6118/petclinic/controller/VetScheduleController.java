package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.dto.request.CreateWeeklyAvailabilityRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateWeeklyAvailabilityRequestDto;
import com.github.farzan6118.petclinic.service.VetScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vets/{vetUuid}/schedule")
@Validated
public class VetScheduleController {

    private final VetScheduleService vetScheduleService;


    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createWeeklySchedule(
            @PathVariable UUID vetUuid,
            @Valid @RequestBody CreateWeeklyAvailabilityRequestDto request
    ) {
        vetScheduleService.createWeeklyAvailability(
                vetUuid,
                request
        );
    }


    @PutMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateWeeklySchedule(
            @PathVariable UUID vetUuid,
            @PathVariable Long scheduleId,
            @Valid @RequestBody UpdateWeeklyAvailabilityRequestDto request
    ) {
        vetScheduleService.updateWeeklyAvailability(
                vetUuid,
                scheduleId,
                request
        );
    }


    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWeeklySchedule(
            @PathVariable UUID vetUuid,
            @PathVariable Long scheduleId
    ) {
        vetScheduleService.deleteWeeklyAvailability(
                vetUuid,
                scheduleId
        );
    }
}