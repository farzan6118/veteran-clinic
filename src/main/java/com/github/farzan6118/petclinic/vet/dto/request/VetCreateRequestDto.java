package com.github.farzan6118.petclinic.vet.dto.request;

import com.github.farzan6118.petclinic.person.dto.request.AddressCreateRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.PersonCreateRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.ProfileCreateRequestDto;

public record VetCreateRequestDto(
        PersonCreateRequestDto person,
        ProfileCreateRequestDto profile,
        AddressCreateRequestDto address
) {
}
