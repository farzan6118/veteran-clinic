package com.github.farzan6118.petclinic.mapper;

import com.github.farzan6118.petclinic.dto.request.VisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.model.Pet;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.Visit;
import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import org.springframework.stereotype.Component;

@Component
public class VisitMapper {
    public VisitResponseDto toResponse(Visit visit) {
        if (visit == null) {
            return null;
        }
        return new VisitResponseDto(visit.getUuid(),
                visit.getPet().getUuid(),
                visit.getVet().getUuid(),
                visit.getVisitDateTime(),
                visit.getDescription(),
                visit.getStatus()
        );
    }

    public Visit mapToVisitEntity(VisitRequestDto request, Pet pet, Vet vet) {
        Visit visit = new Visit();
        visit.setPet(pet);
        visit.setVet(vet);
        visit.setVisitDateTime(request.visitDateTime());
        visit.setDescription(request.description());
        visit.setStatus(VisitStatus.BOOKED);
        return visit;
    }
}