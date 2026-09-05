package com.github.farzan6118.petclinic.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateVetAvailabilityRequestDto(
        @NotNull(message = "date.is.required")
        @FutureOrPresent(message = "invalid.date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(example = "2026-10-10")
        LocalDate date,

        @NotNull(message = "start.time.is.required")
        @JsonFormat(pattern = "HH:mm")
        @Schema(example = "09:00")
        LocalTime startTime,

        @NotNull(message = "end.time.is.required")
        @JsonFormat(pattern = "HH:mm")
        @Schema(example = "17:00")
        LocalTime endTime
) {
}
