package com.github.farzan6118.petclinic.vet.repository;

import com.github.farzan6118.petclinic.appointment.model.QVisit;
import com.github.farzan6118.petclinic.vet.model.QVet;
import com.github.farzan6118.petclinic.vet.model.Vet;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VetQueryRepositoryImpl implements VetQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Vet> findAvailableVets(LocalDateTime start, LocalDateTime end) {

        QVet vet = QVet.vet;
        QVisit visit = QVisit.visit;

        return queryFactory
                .selectFrom(vet)
                .where(
                        JPAExpressions
                                .selectOne()
                                .from(visit)
                                .where(
                                        visit.vet.eq(vet),
                                        visit.startTime.lt(end),
                                        visit.endTime.gt(start)
                                )
                                .notExists()
                )
                .fetch();
    }
}
