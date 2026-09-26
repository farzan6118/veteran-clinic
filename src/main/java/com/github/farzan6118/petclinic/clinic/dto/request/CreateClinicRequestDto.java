package com.github.farzan6118.petclinic.clinic.dto.request;

import com.github.farzan6118.petclinic.person.dto.request.AddressCreateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateClinicRequestDto(
        @Valid @NotNull AddressCreateRequestDto address,
        @NotNull Boolean active
) {
}
