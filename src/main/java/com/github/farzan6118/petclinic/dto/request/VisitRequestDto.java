package com.github.farzan6118.petclinic.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record VisitRequestDto(
        @NotNull(message = "pet.uuid.is.required")
        UUID petUuid,
        @NotNull(message = "vet.uuid.is.required")
        UUID vetUuid,
        @NotNull(message = "slot.uuid.is.required")
        UUID slotUuid,
        @Size(max = 2048, message = "description.too.long")
        String description

) {
}
