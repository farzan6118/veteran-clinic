//package com.github.farzan6118.petclinic.repository.jooq;
//
//import com.github.farzan6118.petclinic.dto.response.VisitResponseDto;
//import com.github.farzan6118.petclinic.model.Vet;
//import com.github.farzan6118.petclinic.model.constant.VisitStatus;
//import com.github.farzan6118.petclinic.repository.PetRepository;
//import com.github.farzan6118.petclinic.repository.VetRepository;
//import com.github.farzan6118.petclinic.repository.VisitRepository;
//
//import org.jooq.Condition;
//import org.jooq.DSLContext;
//import org.jooq.impl.DSL;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
//public class VisitQueryRepository {
//    private final DSLContext dsl;
//    private final VetRepository vetRepository;
//    private final PetRepository petRepository;
//    private final VisitRepository visitRepository;
//
//    public VisitQueryRepository(DSLContext dsl, VetRepository vetRepository, PetRepository petRepository, VisitRepository visitRepository) {
//        this.dsl = dsl;
//        this.vetRepository = vetRepository;
//        this.petRepository = petRepository;
//        this.visitRepository = visitRepository;
//    }
//
//    public List<VisitResponseDto> findVisits(
//            UUID vetUuid,
//            VisitStatus status,
//            LocalDate from,
//            LocalDate to
//    ) {
//
//        Condition condition = DSL.noCondition();
//
//        if (vetUuid != null) {
//            condition = condition.and(VET.UUID.eq(vetUuid));
//        }
//
//        if (status != null) {
//            condition = condition.and(
//                    VISIT.STATUS.eq(status.name())
//            );
//        }
//
//        if (from != null) {
//            condition = condition.and(
//                    VISIT.DATE.ge(from)
//            );
//        }
//
//        if (to != null) {
//            condition = condition.and(
//                    VISIT.DATE.le(to)
//            );
//        }
//
//        return dsl
//                .select(
//                        VISIT.UUID,
//                        VISIT.DATE,
//                        VISIT.START_TIME,
//                        VISIT.END_TIME,
//                        VISIT.STATUS,
//                        PET.UUID.as("petUuid"),
//                        VET.UUID.as("vetUuid")
//                )
//                .from(VISIT)
//                .join(PET)
//                .on(PET.ID.eq(VISIT.PET_ID))
//                .join(VET)
//                .on(VET.ID.eq(VISIT.VET_ID))
//                .where(condition)
//                .orderBy(
//                        VISIT.DATE.desc(),
//                        VISIT.START_TIME.desc()
//                )
//                .fetchInto(VisitResponseDto.class);
//    }
//
//}
