package com.github.farzan6118.petclinic.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateVetAvailabilityRequestDto(
        @NotNull
        LocalDate onDate,
        @NotNull
        LocalTime availableFrom,
        @NotNull
        LocalTime availableTo,
        @NotNull
        Integer timeSlice
) {
}
