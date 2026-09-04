package com.github.farzan6118.petclinic.mapper;

import com.github.farzan6118.petclinic.dto.request.CreateSpeciesRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateSpeciesRequestDto;
import com.github.farzan6118.petclinic.dto.response.SpeciesResponseDto;
import com.github.farzan6118.petclinic.model.Species;
import org.springframework.stereotype.Component;

@Component
public class SpeciesMapper {

    public void mapToSpecies(CreateSpeciesRequestDto request, Species species) {
        species.setName(request.name());
        species.setCode(request.code());
        species.setOrigin(request.origin());
        species.setDescription(request.description());
    }
    public void mapToSpecies(UpdateSpeciesRequestDto request, Species species) {
        species.setName(request.name());
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
}
