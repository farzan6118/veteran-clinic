package com.github.farzan6118.petclinic.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateVetRequestDto(
        @Schema(example = "John")
        String firstname,
        @Schema(example = "Due")
        String lastname,
        @Schema(example = "6311001012")
        String nationalCode,
        @Schema(example = "09121111111")
        @NotBlank(message = "telephone.is.required")
        String telephone,
        @Schema(example = "vet@test.com")
        @NotBlank(message = "email.is.required")
        @Email
        String email
) {
}
