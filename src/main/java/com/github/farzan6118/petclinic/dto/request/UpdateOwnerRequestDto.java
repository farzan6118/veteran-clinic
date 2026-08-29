package com.github.farzan6118.petclinic.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateOwnerRequestDto(
        @Schema(example = "John")
        String firstname,
        @Schema(example = "Due")
        String lastname,
        @Schema(example = "6311001012")
        String nationalCode,
        @Schema(example = "Long St.")
        String address,
        @Schema(example = "Tehran")
        String city,
        @Schema(example = "09121111111")
        @NotBlank(message = "telephone.is.required")
        String telephone,
        @Past(message = "invalid.birth.date")
        @Schema(example = "2008-08-29")
        LocalDate birthDate,
        @Schema(example = "owner@test.com")
        @NotBlank(message = "email.is.required")
        @Email
        String email
) {
}