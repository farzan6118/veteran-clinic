package com.github.farzan6118.petclinic.owner.dto.request;

import com.github.farzan6118.petclinic.person.dto.request.AddressCreateRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.PersonCreateRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.ProfileCreateRequestDto;

public record OwnerCreateRequestDto(
        PersonCreateRequestDto person,
        ProfileCreateRequestDto profile,
        AddressCreateRequestDto address
) {
}
