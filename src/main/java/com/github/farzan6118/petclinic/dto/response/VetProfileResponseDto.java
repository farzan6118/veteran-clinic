package com.github.farzan6118.petclinic.dto.response;

import com.github.farzan6118.petclinic.model.constant.AppointmentDuration;

import java.time.LocalDate;
import java.util.UUID;

public record VetProfileResponseDto(
        UUID uuid,
        String fullName,
        String nationalCode,
        String telephone,
        String email,
        AppointmentDuration AppointmentDuration,
        String city,
        String address,
        String specialty,
        LocalDate birthDate

) {
}
