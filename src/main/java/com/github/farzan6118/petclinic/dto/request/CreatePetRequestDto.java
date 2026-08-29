package com.github.farzan6118.petclinic.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePetRequestDto(
        @Schema(example = "Jessy")
        @NotBlank(message = "pet.name.is.required")
        String name,
        @Past(message = "invalid.birth.date")
        @Schema(example = "2024-08-29")
        LocalDate birthDate,
        @NotNull(message = "pet.type.is.required")
        UUID petTypeUuid,
        @NotNull(message = "owner.is.required")
        UUID ownerUuid
) {
}
