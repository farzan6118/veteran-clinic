package com.github.farzan6118.petclinic.visit.repository;

import com.github.farzan6118.petclinic.visit.dto.request.VisitAdvancedSearch;
import com.github.farzan6118.petclinic.visit.model.QVisit;
import com.github.farzan6118.petclinic.visit.model.Visit;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VisitQueryRepositoryImpl implements VisitQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public Page<Visit> advancedSearch(VisitAdvancedSearch request, Pageable pageable) {

        QVisit visit = QVisit.visit;

        BooleanBuilder predicate = new BooleanBuilder();

        addDateRangePredicate(
                predicate,
                visit,
                request.startDateTime(),
                request.endDateTime()
        );

        addUuidPredicate(
                predicate,
                visit.vet.uuid,
                request.vetUuid()
        );

        addUuidPredicate(
                predicate,
                visit.pet.uuid,
                request.petUuid()
        );

        addUuidPredicate(
                predicate,
                visit.room.uuid,
                request.roomUuid()
        );

        Long total = queryFactory
                .select(visit.count())
                .from(visit)
                .where(predicate)
                .fetchOne();

        List<Visit> content = queryFactory
                .selectFrom(visit)
                .where(predicate)
                .orderBy(getOrderSpecifier(visit, pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private void addDateRangePredicate(
            BooleanBuilder predicate,
            QVisit visit,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {

        if (startDateTime != null) {
            predicate.and(visit.startTime.goe(startDateTime));
        }

        if (endDateTime != null) {
            predicate.and(visit.startTime.lt(endDateTime));
        }
    }

    private void addUuidPredicate(
            BooleanBuilder predicate,
            SimpleExpression<UUID> path,
            UUID value) {

        if (value != null) {
            predicate.and(path.eq(value));
        }
    }

    private OrderSpecifier<?> getOrderSpecifier(QVisit visit, Pageable pageable) {

        Sort.Order sortOrder = pageable.getSort().getOrderFor("createdDate");

        if (sortOrder != null) {
            return new OrderSpecifier<>(
                    sortOrder.isAscending()
                            ? Order.ASC
                            : Order.DESC,
                    visit.createdDate
            );
        }

        sortOrder = pageable.getSort().getOrderFor("lastModifiedDate");

        if (sortOrder != null) {
            return new OrderSpecifier<>(
                    sortOrder.isAscending()
                            ? Order.ASC
                            : Order.DESC,
                    visit.lastModifiedDate
            );
        }

        return new OrderSpecifier<>(Order.DESC, visit.createdDate);
    }

    @Override
    public List<Visit> findAllByDateAndTimeBetween(
            UUID vetUuid,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay) {

        return queryFactory
                .selectFrom(QVisit.visit)
                .where(
                        QVisit.visit.vet.uuid.eq(vetUuid),
                        QVisit.visit.startTime.goe(startOfDay),
                        QVisit.visit.startTime.lt(endOfDay)
                )
                .fetch();
    }

    @Override
    public List<Visit> findAllByRoomUuidAndStartTimeBetween(
            UUID roomUuid,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay) {

        return queryFactory
                .selectFrom(QVisit.visit)
                .where(
                        QVisit.visit.room.uuid.eq(roomUuid),
                        QVisit.visit.startTime.goe(startOfDay),
                        QVisit.visit.startTime.lt(endOfDay)
                )
                .fetch();
    }
}