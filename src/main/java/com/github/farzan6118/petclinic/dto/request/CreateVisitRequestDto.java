package com.github.farzan6118.petclinic.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateVisitRequestDto(
        UUID petUuid,
        UUID vetUuid,
        LocalDateTime visitDateTime,
        String description

) {
}
