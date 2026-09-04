package com.github.farzan6118.petclinic.dto.request;

import com.github.farzan6118.petclinic.model.constant.AppointmentDuration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateVetRequestDto(
        @Schema(example = "John")
        String firstName,
        @Schema(example = "Due")
        String lastName,
        @Schema(example = "6311001012")
        String nationalId,
        @Schema(example = "09121111111")
        @NotBlank(message = "mobileNumber.is.required")
        String mobileNumber,
        @Schema(example = "vet@test.com")
        @NotBlank(message = "email.is.required")
        @Email
        String email,
        @Schema(example = "FIFTEEN_MINUTES",
                allowableValues = {
                        "FIFTEEN_MINUTES",
                        "THIRTY_MINUTES",
                        "FORTY_FIVE_MINUTES",
                        "ONE_HOUR",
                        "NINETY_MINUTES",
                        "TWO_HOURS"
                })
        AppointmentDuration duration
) {
}
