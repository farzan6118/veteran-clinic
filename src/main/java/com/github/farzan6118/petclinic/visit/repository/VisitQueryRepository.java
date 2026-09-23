package com.github.farzan6118.petclinic.visit.repository;

import com.github.farzan6118.petclinic.visit.dto.request.VisitAdvancedSearch;
import com.github.farzan6118.petclinic.visit.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitQueryRepository {

    Page<Visit> advancedSearch(VisitAdvancedSearch request, Pageable pageable);

}
