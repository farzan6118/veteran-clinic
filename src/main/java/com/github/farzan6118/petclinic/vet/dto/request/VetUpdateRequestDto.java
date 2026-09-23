package com.github.farzan6118.petclinic.vet.dto.request;

import com.github.farzan6118.petclinic.person.dto.request.AddressUpdateRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.PersonUpdateRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.ProfileUpdateRequestDto;

public record VetUpdateRequestDto(
        PersonUpdateRequestDto person,
        ProfileUpdateRequestDto profile,
        AddressUpdateRequestDto address
) {
}
