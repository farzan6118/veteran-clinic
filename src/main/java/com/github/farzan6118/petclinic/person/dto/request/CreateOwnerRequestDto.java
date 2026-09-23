package com.github.farzan6118.petclinic.person.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CreateOwnerRequestDto(
        @Schema(example = "John")
        String firstName,
        @Schema(example = "Due")
        String lastName,
        @Schema(example = "owner@test.com")
        @NotBlank(message = "email.is.required")
        @Email
        String email,
        @Schema(example = "09121111111")
        @NotBlank(message = "mobile.mumber.is.required")
        String mobileNumber,
        @Schema(example = "7501110001")
        String nationalId,
        @Past(message = "invalid.birth.date")
        @Schema(example = "2008-08-29")
        LocalDate birthDate,
        @Schema(example = "Long St.")
        String address,
        @Schema(example = "Tehran")
        String city

) {
}
