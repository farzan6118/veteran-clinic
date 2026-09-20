package com.github.farzan6118.petclinic.visit.repository;

import com.github.farzan6118.petclinic.visit.dto.request.VisitAdvancedSearch;
import com.github.farzan6118.petclinic.visit.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VisitQueryRepository {

    Page<Visit> advancedSearch(VisitAdvancedSearch request, Pageable pageable);

    List<Visit> findAllByDateAndTimeBetween(
            UUID vetUuid,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    List<Visit> findAllByRoomUuidAndStartTimeBetween(
            UUID roomUuid,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
}
