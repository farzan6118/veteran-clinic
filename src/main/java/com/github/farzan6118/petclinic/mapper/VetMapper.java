package com.github.farzan6118.petclinic.mapper;

import com.github.farzan6118.petclinic.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetResponseDto;
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
        vet.setTelephone(request.telephone());
        vet.setEmail(request.email());
        vet.setSpecialty(request.specialty());
        vet.setBirthDate(request.birthDate());
        vet.setAddress(request.address());
        vet.setCity(request.city());

        return vet;
    }

    public void mapToEntity(UpdateVetRequestDto request, Vet vet) {
        vet.setFirstname(request.firstname());
        vet.setLastname(request.lastname());
        vet.setNationalCode(request.nationalCode());
        vet.setTelephone(request.telephone());
        vet.setEmail(request.email());
        vet.setSpecialty(request.specialty());
        vet.setBirthDate(request.birthDate());
        vet.setAddress(request.address());
        vet.setCity(request.city());
    }

    public VetResponseDto mapToDto(Vet vet) {
        return new VetResponseDto(
                vet.getUuid(),
                vet.getFirstname(),
                vet.getLastname(),
                vet.getNationalCode(),
                vet.getTelephone(),
                vet.getEmail(),
                vet.getSpecialty(),
                vet.getBirthDate(),
                vet.getAddress(),
                vet.getCity());
    }
}
