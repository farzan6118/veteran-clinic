package com.github.farzan6118.petclinic.vet.mapper;

import com.github.farzan6118.petclinic.vet.dto.response.AvailabilityResponseDto;
import com.github.farzan6118.petclinic.vet.model.VetAvailability;
import org.springframework.stereotype.Component;

@Component
public class VetAvailabilityMapper {

    public AvailabilityResponseDto mapToDto(VetAvailability vetAvailability) {
        return new AvailabilityResponseDto(
                vetAvailability.getUuid(),
                vetAvailability.getTimeRange().getStartDate(),
                vetAvailability.getTimeRange().getStartTime(),
                vetAvailability.getTimeRange().getEndTime(),
                vetAvailability.isActive());
    }
}
