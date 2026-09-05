package com.github.farzan6118.petclinic.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateVetAvailabilityRequestDto(
        @NotNull
        @Schema(example = "2026-10-10")
        @FutureOrPresent
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull
        @Schema(example = "09:00")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @NotNull
        @Schema(example = "17:00")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime
) {
}
