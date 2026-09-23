package com.github.farzan6118.petclinic.vet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateVetRequestDto(
        @Schema(example = "John")
        String firstName,
        @Schema(example = "Due")
        String lastName,
        @Schema(example = "7501110001")
        String nationalId
) {
}
