package com.github.farzan6118.petclinic.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public record VetProfileResponseDto(
        UUID uuid,
        String fullName,
        String nationalId,
        String mobileNumber,
        String email,
        String city,
        String address,
        String specialty,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate

) {
}
