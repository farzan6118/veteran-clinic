package com.github.farzan6118.petclinic.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateSpeciesRequestDto(
        @NotBlank(message = "pet.type.is.required")
        @Schema(example = "cat")
        String name,
        @Schema(example = "100-001")
        String code,
        @Schema(example = "Iran")
        String origin,
        @Schema(example = "Persian cat")
        String description
) {
}
