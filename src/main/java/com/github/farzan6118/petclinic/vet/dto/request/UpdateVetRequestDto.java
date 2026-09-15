package com.github.farzan6118.petclinic.vet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateVetRequestDto(
        @Schema(example = "John")
        String firstName,
        @Schema(example = "Due")
        String lastName,
        @Schema(example = "7501110001")
        String nationalId,
        @Schema(example = "09121111111")
        @NotBlank(message = "mobileNumber.is.required")
        String mobileNumber,
        @Schema(example = "vet@test.com")
        @NotBlank(message = "email.is.required")
        @Email
        String email
) {
}
