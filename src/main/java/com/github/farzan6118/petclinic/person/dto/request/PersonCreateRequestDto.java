package com.github.farzan6118.petclinic.person.dto.request;

public record PersonCreateRequestDto(
        String title,
        String firstName,
        String lastName,
        String nationalId
) {
}
