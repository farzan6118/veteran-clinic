package com.github.farzan6118.petclinic.person.dto.request;

import java.time.LocalDate;

public record ProfileCreateRequestDto(
        String email,
        String mobileNumber,
        LocalDate birthDate,
        String photo
) {
}
