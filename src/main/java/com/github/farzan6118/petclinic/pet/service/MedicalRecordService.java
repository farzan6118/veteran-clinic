package com.github.farzan6118.petclinic.pet.service;

import com.github.farzan6118.petclinic.appointment.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.appointment.model.Visit;
import com.github.farzan6118.petclinic.pet.model.MedicalRecord;

public interface MedicalRecordService {

    void create(MedicalRecord medicalRecord, CompleteVisitRequestDto request, Visit visit);
}
