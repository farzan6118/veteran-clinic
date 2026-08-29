package com.github.farzan6118.petclinic.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateVisitRequestDto(
        @NotNull(message = "pet.is.required")
        UUID petUuid,
        @NotNull(message = "vet.is.required")
        UUID vetUuid,
        @NotNull(message = "visit.date.time.is.required")
        @Future(message = "visit.date.time.must.be.in.future")
        LocalDateTime visitDateTime,
        @Size(max = 2048, message = "description.too.long")
        String description

) {
}
