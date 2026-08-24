package com.github.farzan6118.petclinic.controller.dto.request;

public record CreatePetTypeRequestDto(
        String name, String code, String description
) {
}
