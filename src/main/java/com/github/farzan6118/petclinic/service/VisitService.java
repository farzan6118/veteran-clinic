package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.controller.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.controller.dto.request.CreateVisitRequest;
import com.github.farzan6118.petclinic.controller.dto.response.VisitResponse;

import java.util.List;
import java.util.UUID;

public interface VisitService {

    VisitResponse bookVisit(CreateVisitRequest request);

    List<VisitResponse> getMyVisits();

    VisitResponse getByUuid(UUID uuid);

    void cancelVisit(UUID uuid);

    List<VisitResponse> getVetVisits();

    VisitResponse completeVisit(UUID uuid,CompleteVisitRequest request);
}
