package com.github.farzan6118.petclinic.visit.service;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.visit.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.CreateVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.VisitAdvancedSearch;
import com.github.farzan6118.petclinic.visit.dto.response.VisitResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VisitService {

    void bookVisit(CreateVisitRequestDto request);

    VisitResponseDto getByUuid(UUID uuid);

    void cancelVisit(UUID uuid, String reason);

    List<VisitResponseDto> findAllVisitsByVetUuid(UUID vetUuid, LocalDate date);

    List<VisitResponseDto> findAllVisitsByPetUuid(UUID petUuid, LocalDate date);

    List<VisitResponseDto> findAllVisitsByRoomUuid(UUID RoomUuid, LocalDate date);

    void completeVisit(UUID uuid, CompleteVisitRequestDto request);

    PageResponseDto<VisitResponseDto> findAll(PageAndSortRequestDto requestDto);

    void rescheduleVisit(UUID uuid, RescheduleVisitRequestDto request);

    PageResponseDto<VisitResponseDto> advancedSearch(VisitAdvancedSearch request);
}
