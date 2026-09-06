package com.github.farzan6118.petclinic.dto.response;

import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.model.constant.VisitType;

import java.time.LocalDateTime;
import java.util.UUID;

public record VisitResponseDto(
        UUID uuid,
        UUID petUuid,
        UUID vetUuid,
        LocalDateTime visitDateTime,
        LocalDateTime visitEndDateTime,
        VisitType visitType,
        UUID roomUuid,
        String description,
        VisitStatus status
) {
}
