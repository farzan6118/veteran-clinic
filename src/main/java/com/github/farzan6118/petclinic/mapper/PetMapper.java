package com.github.farzan6118.petclinic.mapper;

import com.github.farzan6118.petclinic.dto.request.CreatePetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdatePetRequestDto;
import com.github.farzan6118.petclinic.dto.response.PetResponseDto;
import com.github.farzan6118.petclinic.model.Owner;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Species;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public void mapToPet(UpdatePetRequestDto request, Owner owner, Pet pet, Species species) {
        pet.setName(request.name());
        pet.setColor(request.color());
        pet.setMarks(request.marks());
        pet.setSex(request.sex());
        pet.setBirthDate(request.birthDate());
        pet.setSpecies(species);
        pet.setOwner(owner);
    }

    public void mapToPet(CreatePetRequestDto request, Owner owner, Pet pet, Species species) {
        pet.setName(request.name());
        pet.setColor(request.color());
        pet.setMarks(request.marks());
        pet.setSex(request.sex());
        pet.setBirthDate(request.birthDate());
        pet.setSpecies(species);
        pet.setOwner(owner);
    }

    public PetResponseDto mapToDto(Pet pet) {
        return new PetResponseDto(
                pet.getUuid(),
                pet.getName(),
                pet.getColor(),
                pet.getMarks(),
                pet.getSex(),
                pet.getBirthDate(),
                pet.getSpecies().getName()



        );
    }
}
