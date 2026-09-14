package com.github.farzan6118.petclinic.vet.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AvailabilityResponseDto(
        UUID uuid,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,
        Boolean active
) {
}
