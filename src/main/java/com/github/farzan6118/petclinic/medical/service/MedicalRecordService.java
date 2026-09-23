package com.github.farzan6118.petclinic.medical.service;

import com.github.farzan6118.petclinic.medical.model.MedicalRecord;
import com.github.farzan6118.petclinic.visit.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.visit.model.Visit;

public interface MedicalRecordService {

    void create(MedicalRecord medicalRecord, CompleteVisitRequestDto request, Visit visit);
}
