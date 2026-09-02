package com.github.farzan6118.petclinic.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record VetProfileUpdateRequestDto(
        String specialty,
        @Past(message = "invalid.birth.date")
        @Schema(example = "2008-08-29")
        LocalDate birthDate,
        @Schema(example = "Long St.")
        String address,
        @Schema(example = "Tehran")
        String city) {
}
