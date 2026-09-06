package com.github.farzan6118.petclinic.dto.response;

import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import com.github.farzan6118.petclinic.model.constant.VisitType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record VisitResponseDto(
        UUID uuid,
        UUID petUuid,
        String petName,
        String species,
        String ownerFullName,
        UUID vetUuid,
        String vetFullName,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        VisitType visitType,
        UUID roomUuid,
        String roomName,
        String description,
        VisitStatus status
) {
}
