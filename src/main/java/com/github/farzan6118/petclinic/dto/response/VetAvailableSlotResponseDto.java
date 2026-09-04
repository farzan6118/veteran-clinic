package com.github.farzan6118.petclinic.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record VetAvailableSlotResponseDto(
        UUID uuid,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
) {
}