package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.dto.request.CreateVisitRequestDto;
import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;

import java.util.List;
import java.util.UUID;

public interface VisitService {

    void bookVisit(CreateVisitRequestDto request);

    List<VisitResponseDto> getMyVisits();

    VisitResponseDto getByUuid(UUID uuid);

    void cancelVisit(UUID uuid, String reason);

    List<VisitResponseDto> getVetVisits();

    VisitResponseDto completeVisit(UUID uuid, CompleteVisitRequest request);

    List<VisitResponseDto> getAllVisits();
}
