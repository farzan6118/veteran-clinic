package com.github.farzan6118.petclinic.controller.dto.request;

public record CreateOwnerRequestDto(
        String firstname,
        String lastname,
        String address,
        String city,
        String telephone,
        String email
) {
}
