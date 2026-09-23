package com.github.farzan6118.petclinic.appointment.repository;

import com.github.farzan6118.petclinic.appointment.dto.request.VisitAdvancedSearch;
import com.github.farzan6118.petclinic.appointment.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitQueryRepository {

    Page<Visit> advancedSearch(VisitAdvancedSearch request, Pageable pageable);

}
