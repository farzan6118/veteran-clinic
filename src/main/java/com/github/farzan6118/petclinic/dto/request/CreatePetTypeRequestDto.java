package com.github.farzan6118.petclinic.dto.request;

public record CreatePetTypeRequestDto(
        String name, String code, String description
) {
}
