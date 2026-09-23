package com.github.farzan6118.petclinic.vet.mapper;

import com.github.farzan6118.petclinic.person.dto.request.PersonCreateRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.PersonUpdateRequestDto;
import com.github.farzan6118.petclinic.person.mapper.AddressMapper;
import com.github.farzan6118.petclinic.person.mapper.PersonMapper;
import com.github.farzan6118.petclinic.person.mapper.ProfileMapper;
import com.github.farzan6118.petclinic.person.model.Person;
import com.github.farzan6118.petclinic.vet.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.vet.model.Vet;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class VetMapper {

    private final PersonMapper personMapper;
    private final ProfileMapper profileMapper;
    private final AddressMapper addressMapper;

    public VetMapper(PersonMapper personMapper, ProfileMapper profileMapper, AddressMapper addressMapper) {
        this.personMapper = personMapper;
        this.profileMapper = profileMapper;
        this.addressMapper = addressMapper;
    }

    public VetResponseDto toDto(Vet vet) {
        Person person = vet.getPerson();
        return new VetResponseDto(
                vet.getUuid(),
                personMapper.toDto(person),
                profileMapper.toDto(person.getProfile()),
                addressMapper.toDto(person.getAddress())
        );
    }

    public Person toEntity(PersonCreateRequestDto request) {
        Person person = new Person();
        toEntity(request, person);
        return person;
    }

    public void toEntity(PersonCreateRequestDto request, Person person) {
        person.setTitle(toUpper(request.title()));
        person.setFirstName(toLower(request.firstName()));
        person.setLastName(toLower(request.lastName()));
        person.setNationalId(request.nationalId());
    }

    public void toEntity(PersonUpdateRequestDto request, Person person) {
        person.setTitle(toUpper(request.title()));
        person.setFirstName(toLower(request.firstName()));
        person.setLastName(toLower(request.lastName()));
        person.setNationalId(request.nationalId());
    }

    private String toUpper(String value) {
        return value == null ? null : value.toUpperCase(Locale.ROOT).trim();
    }

    private String toLower(String value) {
        return value == null ? null : value.toLowerCase(Locale.ROOT).trim();
    }
}
