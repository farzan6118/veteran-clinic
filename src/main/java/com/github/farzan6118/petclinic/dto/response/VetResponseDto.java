package com.github.farzan6118.petclinic.dto.response;

import com.github.farzan6118.petclinic.model.constant.AppointmentDuration;

import java.util.UUID;

public record VetResponseDto(
        UUID uuid,
        String fullName,
        String nationalCode,
        String telephone,
        String email,
        AppointmentDuration appointmentDuration
) {
}
