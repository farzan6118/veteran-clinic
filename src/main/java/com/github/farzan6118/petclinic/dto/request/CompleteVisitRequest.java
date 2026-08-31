package com.github.farzan6118.petclinic.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CompleteVisitRequest(
        @Schema(example = "Hashimoto")
        @NotBlank(message = "diagnosis.is.required")
        String diagnosis,
        @Schema(example = "Hashimoto")
        String notes

) {
}
