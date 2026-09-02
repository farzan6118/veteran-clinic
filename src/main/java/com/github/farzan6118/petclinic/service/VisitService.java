package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.dto.request.VisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetAvailableSlotResponseDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VisitService {

    void bookVisit(VisitRequestDto request);

    List<VisitResponseDto> getMyVisits();

    VisitResponseDto getByUuid(UUID uuid);

    void cancelVisit(UUID uuid, String reason);

    List<VisitResponseDto> getVetVisits();

    VisitResponseDto completeVisit(UUID uuid, CompleteVisitRequest request);

    List<VisitResponseDto> getAllVisits();

    void rescheduleVisit(UUID uuid, RescheduleVisitRequestDto request);

    List<VetAvailableSlotResponseDto> getAvailableSlots(UUID vetUuid, LocalDate date);

}
