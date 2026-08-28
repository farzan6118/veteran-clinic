package com.github.farzan6118.petclinic.dto.request;

public record UpdatePetTypeRequestDto(
        String name, String code, String description
) {
}
