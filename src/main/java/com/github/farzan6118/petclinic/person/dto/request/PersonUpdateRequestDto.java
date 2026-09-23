package com.github.farzan6118.petclinic.person.dto.request;

public record PersonUpdateRequestDto(
        String title,
        String firstName,
        String lastName,
        String nationalId
) {
}
