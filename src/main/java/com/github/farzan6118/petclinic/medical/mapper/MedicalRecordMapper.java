package com.github.farzan6118.petclinic.medical.mapper;

import com.github.farzan6118.petclinic.common.enums.MedicalRecordType;
import com.github.farzan6118.petclinic.medical.dto.response.MedicalRecordResponseDto;
import com.github.farzan6118.petclinic.medical.model.MedicalRecord;
import com.github.farzan6118.petclinic.visit.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.visit.model.Visit;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {

    public MedicalRecordResponseDto toResponse(MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }

        return new MedicalRecordResponseDto(
                medicalRecord.getUuid(),
                medicalRecord.getPet().getUuid(),
                medicalRecord.getPet().getName(),
                medicalRecord.getVisit().getUuid(),
                medicalRecord.getVet().getUuid(),
                medicalRecord.getVet().getFullName(),
                medicalRecord.getType(),
                medicalRecord.getTitle(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getClinicalNotes(),
                medicalRecord.getTreatmentPlan(),
                medicalRecord.getPrescription(),
                medicalRecord.isFollowUpRequired(),
                medicalRecord.getFollowUpDate(),
                medicalRecord.getVaccinationDetails(),
                medicalRecord.getSurgeryDetails(),
                medicalRecord.getCreatedDate()
        );
    }

    public void toEntity(MedicalRecord medicalRecord, CompleteVisitRequestDto request, Visit visit) {
        medicalRecord.setPet(visit.getPet());
        medicalRecord.setVisit(visit);
        medicalRecord.setVet(visit.getVet());
        medicalRecord.setType(request.recordType() != null
                ? request.recordType()
                : MedicalRecordType.CONSULTATION);
        medicalRecord.setTitle("Visit completion");
        medicalRecord.setDiagnosis(request.diagnosis());
        medicalRecord.setClinicalNotes(request.notes());
        medicalRecord.setTreatmentPlan(request.treatmentPlan());
        medicalRecord.setPrescription(request.prescription());
        medicalRecord.setFollowUpRequired(request.followUpRequired());
        medicalRecord.setFollowUpDate(request.followUpDate());
        medicalRecord.setVaccinationDetails(request.vaccinationDetails());
        medicalRecord.setSurgeryDetails(request.surgeryDetails());
    }
}
