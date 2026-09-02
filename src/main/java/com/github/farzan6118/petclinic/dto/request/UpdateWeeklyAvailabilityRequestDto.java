package com.github.farzan6118.petclinic.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record UpdateWeeklyAvailabilityRequestDto(

        @NotNull
        DayOfWeek dayOfWeek,

        @NotNull
        LocalTime availableFrom,

        @NotNull
        LocalTime availableTo,

        @NotNull
        @Positive
        Integer durationMinutes
) {
}