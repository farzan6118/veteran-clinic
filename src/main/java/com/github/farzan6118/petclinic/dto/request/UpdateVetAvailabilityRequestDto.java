package com.github.farzan6118.petclinic.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateVetAvailabilityRequestDto(
        @NotNull
        @Schema(example = "2026-10-10")
        @FutureOrPresent
        LocalDate date,

        @NotNull
        @Schema(example = "09:00")
        LocalTime startTime,

        @NotNull
        @Schema(example = "17:00")
        LocalTime endTime
) {
}
