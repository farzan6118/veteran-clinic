package com.github.farzan6118.petclinic.dto.response;

import java.time.Duration;
import java.util.UUID;

public record VetResponseDto(
        UUID uuid,
        String fullName,
        String nationalId,
        String mobileNumber,
        String email,
        Duration duration
) {
}
