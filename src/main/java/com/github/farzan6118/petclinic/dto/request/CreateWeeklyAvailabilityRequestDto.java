package com.github.farzan6118.petclinic.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateWeeklyAvailabilityRequestDto(

        @NotNull
        LocalDate date,

        @NotNull
        LocalTime availableFrom,

        @NotNull
        LocalTime availableTo,

        @NotNull
        @Positive
        Integer durationMinutes
) {
}