package com.github.farzan6118.petclinic.clinic.dto.request;

import com.github.farzan6118.petclinic.person.dto.request.AddressUpdateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateClinicRequestDto(
        @Valid @NotNull AddressUpdateRequestDto address,
        @NotNull Boolean active
) {
}
