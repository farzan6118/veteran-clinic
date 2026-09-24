package com.github.farzan6118.petclinic.vet.dto.response;

import java.util.List;
import java.util.UUID;

public record VetAvailableTimeSlot(
        UUID vetUuid,
        String vetName,
        List<TimeInterval> availableIntervals
) {
}