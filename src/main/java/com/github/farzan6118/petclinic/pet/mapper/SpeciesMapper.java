package com.github.farzan6118.petclinic.pet.mapper;

import com.github.farzan6118.petclinic.pet.dto.request.CreateSpeciesRequestDto;
import com.github.farzan6118.petclinic.pet.dto.request.UpdateSpeciesRequestDto;
import com.github.farzan6118.petclinic.pet.dto.response.SpeciesResponseDto;
import com.github.farzan6118.petclinic.pet.model.Species;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SpeciesMapper {

    public void mapToSpecies(CreateSpeciesRequestDto request, Species species) {
        species.setName(normalizeName(request.name()));
        species.setCode(request.code());
        species.setOrigin(request.origin());
        species.setDescription(request.description());
    }

    public void mapToSpecies(UpdateSpeciesRequestDto request, Species species) {
        species.setName(normalizeName(request.name()));
        species.setCode(request.code());
        species.setOrigin(request.origin());
        species.setDescription(request.description());
    }

    public SpeciesResponseDto mapToDto(Species species) {
        return new SpeciesResponseDto(
                species.getUuid(),
                species.getName(),
                species.getCode(),
                species.getOrigin(),
                species.getDescription()
        );
    }

    private String normalizeName(String string) {
        return string.toUpperCase(Locale.ROOT).trim();
    }
}
