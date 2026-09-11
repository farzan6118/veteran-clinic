package com.github.farzan6118.petclinic.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateVetAvailabilityRequestDto(

        @FutureOrPresent
        @NotNull(message = "start.time.is.required")
        LocalDateTime startTime,

        @Future
        @NotNull(message = "end.time.is.required")
        LocalDateTime endTime
) {
}
