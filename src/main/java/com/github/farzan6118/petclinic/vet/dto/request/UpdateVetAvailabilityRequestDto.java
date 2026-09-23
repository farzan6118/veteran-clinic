package com.github.farzan6118.petclinic.vet.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateVetAvailabilityRequestDto(

        @FutureOrPresent
        @NotNull(message = "visit.date.from.is.required")
        LocalDateTime startTime,

        @Future
        @NotNull(message = "visit.date.to.is.required")
        LocalDateTime endTime
) {
}
