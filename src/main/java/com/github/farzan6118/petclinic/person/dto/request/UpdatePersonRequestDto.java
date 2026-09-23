package com.github.farzan6118.petclinic.person.dto.request;

public record UpdatePersonRequestDto(
        String title,
        String firstName,
        String lastName,
        String nationalId,
        CreateProfileRequestDto profile,
        CreateAddressRequestDto address
) {
}