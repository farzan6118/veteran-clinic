package com.github.farzan6118.petclinic.mapper;

import com.github.farzan6118.petclinic.dto.request.CreateOwnerRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateOwnerRequestDto;
import com.github.farzan6118.petclinic.dto.response.OwnerResponseDto;
import com.github.farzan6118.petclinic.model.Owner;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class OwnerMapper {

    public OwnerResponseDto mapToDto(Owner owner) {
        return new OwnerResponseDto(
                owner.getUuid(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail(),
                owner.getMobileNumber(),
                owner.getNationalId(),
                owner.getBirthDate(),
                owner.getCity(),
                owner.getAddress()
        );
    }

    public void mapToOwner(CreateOwnerRequestDto request, Owner owner) {
        owner.setFirstName(normalizeName(request.firstName()));
        owner.setLastName(normalizeName(request.lastName()));
        owner.setAddress(request.address());
        owner.setNationalId(request.nationalId());
        owner.setBirthDate(request.birthDate());
        owner.setCity(normalizeName(request.city()));
        owner.setMobileNumber(request.mobileNumber());
        owner.setEmail(request.email());
    }

    public void mapToOwner(UpdateOwnerRequestDto request, Owner owner) {
        owner.setFirstName(normalizeName(request.firstName()));
        owner.setLastName(normalizeName(request.lastName()));
        owner.setAddress(request.address());
        owner.setNationalId(request.nationalId());
        owner.setBirthDate(request.birthDate());
        owner.setCity(request.city());
        owner.setMobileNumber(request.mobileNumber());
        owner.setEmail(request.email());
    }

    private String normalizeName(String string) {
        return string.toLowerCase(Locale.ROOT).trim();
    }
}
