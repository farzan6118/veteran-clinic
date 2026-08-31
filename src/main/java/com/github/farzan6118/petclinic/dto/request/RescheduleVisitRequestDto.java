package com.github.farzan6118.petclinic.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record RescheduleVisitRequestDto(
        @NotNull(message = "visit.date.time.is.required")
        @Future(message = "visit.date.time.must.be.in.future")
        LocalDateTime visitDateTime,
        @Size(max = 2048, message = "description.too.long")
        String description,
        @Size(max = 255, message = "reason.too.long")
        String reason

) {
}
