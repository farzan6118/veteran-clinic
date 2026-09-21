package com.github.farzan6118.petclinic.visit.service;

import com.github.farzan6118.petclinic.common.dto.request.PageAndSortRequestDto;
import com.github.farzan6118.petclinic.common.dto.response.PageResponseDto;
import com.github.farzan6118.petclinic.visit.dto.request.VisitAdvancedSearch;
import com.github.farzan6118.petclinic.visit.dto.response.VisitResponseDto;

import java.util.UUID;

public interface VisitServiceQuery {

    VisitResponseDto findByUuid(UUID uuid);

    PageResponseDto<VisitResponseDto> findAll(PageAndSortRequestDto requestDto);

    PageResponseDto<VisitResponseDto> advancedSearch(VisitAdvancedSearch request);
}
