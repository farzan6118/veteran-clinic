package com.github.farzan6118.petclinic.owner.mapper;

import com.github.farzan6118.petclinic.owner.dto.request.OwnerCreateRequestDto;
import com.github.farzan6118.petclinic.owner.dto.request.OwnerUpdateRequestDto;
import com.github.farzan6118.petclinic.owner.dto.response.OwnerResponseDto;
import com.github.farzan6118.petclinic.owner.model.Owner;
import com.github.farzan6118.petclinic.person.mapper.AddressMapper;
import com.github.farzan6118.petclinic.person.mapper.PersonMapper;
import com.github.farzan6118.petclinic.person.mapper.ProfileMapper;
import com.github.farzan6118.petclinic.person.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerMapper {

    private final PersonMapper personMapper;
    private final AddressMapper addressMapper;
    private final ProfileMapper profileMapper;

    public OwnerResponseDto toDto(Owner owner) {
        return new OwnerResponseDto(
                owner.getUuid(),
                personMapper.toDto(owner.getPerson()),
                profileMapper.toDto(owner.getPerson().getProfile()),
                addressMapper.toDto(owner.getPerson().getAddress())
        );
    }

    public Owner toEntity(OwnerCreateRequestDto request) {
        Owner owner = new Owner();
        toEntity(request, owner);
        return owner;
    }

    public void toEntity(OwnerCreateRequestDto request, Owner owner) {
        Person person = personMapper.toEntity(request.person());
        person.setProfile(profileMapper.toEntity(request.profile()));
        person.setAddress(addressMapper.toEntity(request.address()));
        owner.setPerson(person);
    }

    public void toEntity(OwnerUpdateRequestDto request, Owner owner) {
        Person person = personMapper.toEntity(request.person());
        person.setProfile(profileMapper.toEntity(request.profile()));
        person.setAddress(addressMapper.toEntity(request.address()));
        owner.setPerson(person);
    }
}
