package com.github.farzan6118.petclinic.person.mapper;

import com.github.farzan6118.petclinic.person.dto.request.CreatePersonRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.UpdatePersonRequestDto;
import com.github.farzan6118.petclinic.person.dto.response.PersonResponseDto;
import com.github.farzan6118.petclinic.person.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    private final AddressMapper addressMapper;
    private final ProfileMapper profileMapper;

    public PersonResponseDto toDto(Person person) {
        return new PersonResponseDto(
                person.getUuid(),
                person.getTitle(),
                person.getFirstName(),
                person.getLastName(),
                person.getNationalId(),
                addressMapper.toDto(person.getAddress()),
                profileMapper.toDto(person.getProfile())
        );
    }

    public Person toEntity(CreatePersonRequestDto request) {
        Person person = new Person();
        toEntity(request, person);
        return person;
    }

    public void toEntity(CreatePersonRequestDto request, Person person) {
        person.setTitle(toUpper(request.title()));
        person.setFirstName(toLower(request.firstName()));
        person.setLastName(toLower(request.lastName()));
        person.setFirstName(request.nationalId());
        person.setProfile(profileMapper.toEntity(request.profile()));
        person.setAddress(addressMapper.toEntity(request.address()));
    }

    public void toEntity(UpdatePersonRequestDto request, Person person) {
        person.setTitle(toUpper(request.title()));
        person.setFirstName(toLower(request.firstName()));
        person.setLastName(toLower(request.lastName()));
        person.setFirstName(request.nationalId());
        person.setProfile(profileMapper.toEntity(request.profile()));
        person.setAddress(addressMapper.toEntity(request.address()));
    }

    private String toUpper(String string) {
        return string.toUpperCase(Locale.ROOT).trim();
    }

    private String toLower(String string) {
        return string.toLowerCase(Locale.ROOT).trim();
    }
}
