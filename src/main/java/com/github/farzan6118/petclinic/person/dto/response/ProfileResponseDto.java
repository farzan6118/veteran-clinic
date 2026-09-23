package com.github.farzan6118.petclinic.person.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record ProfileResponseDto(
        UUID uuid,
        String email,
        String mobileNumber,
        LocalDate birthDate,
        String photo
) {
}
