package com.github.farzan6118.petclinic.mapper;

import com.github.farzan6118.petclinic.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetProfileResponseDto;
import com.github.farzan6118.petclinic.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.model.Profile;
import com.github.farzan6118.petclinic.model.Vet;
import org.springframework.stereotype.Component;

@Component
public class VetMapper {

    public Vet mapToEntity(CreateVetRequestDto request) {
        if (request == null) {
            return null;
        }
        Vet vet = new Vet();
        vet.setFirstname(request.firstname());
        vet.setLastname(request.lastname());
        vet.setNationalCode(request.nationalCode());
        vet.setMobileNumber(request.telephone());
        vet.setEmail(request.email());

        return vet;
    }

    public void mapToEntity(UpdateVetRequestDto request, Vet vet) {
        vet.setFirstname(request.firstname());
        vet.setLastname(request.lastname());
        vet.setNationalCode(request.nationalCode());
        vet.setMobileNumber(request.telephone());
        vet.setEmail(request.email());
    }

    public VetResponseDto mapToDto(Vet vet) {
        return new VetResponseDto(
                vet.getUuid(),
                vet.getFullName(),
                vet.getNationalCode(),
                vet.getMobileNumber(),
                vet.getEmail());
    }

    public VetProfileResponseDto mapToVetProfileDto(Vet vet) {
        Profile profile = vet.getProfile() != null ? vet.getProfile() : new Profile();
        return new VetProfileResponseDto(
                vet.getUuid(),
                vet.getFullName(),
                vet.getNationalCode(),
                vet.getMobileNumber(),
                vet.getEmail(),
                profile.getCity(),
                profile.getAddress(),
                profile.getSpecialty(),
                profile.getBirthDate());
    }
}
