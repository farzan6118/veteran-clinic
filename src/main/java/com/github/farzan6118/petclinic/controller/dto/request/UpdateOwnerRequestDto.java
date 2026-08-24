package com.github.farzan6118.petclinic.controller.dto.request;

public record UpdateOwnerRequestDto(
        String firstname,
        String lastname,
        String address,
        String city,
        String telephone,
        String email
) {
}
