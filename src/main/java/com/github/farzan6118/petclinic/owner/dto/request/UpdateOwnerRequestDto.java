package com.github.farzan6118.petclinic.owner.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record UpdateOwnerRequestDto(
        @Schema(example = "John")
        String firstName,
        @Schema(example = "Due")
        String lastName,
        @Schema(example = "owner@test.com")
        @Email
        String email,
        @Schema(example = "09121111111")
        String mobileNumber,
        @Schema(example = "7501110001")
        String nationalId,
        @PastOrPresent(message = "invalid.birth.date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(example = "2008-08-29")
        LocalDate birthDate,
        @Schema(example = "Tehran")
        String city,
        @Schema(example = "Long St.")
        String address
) {
}