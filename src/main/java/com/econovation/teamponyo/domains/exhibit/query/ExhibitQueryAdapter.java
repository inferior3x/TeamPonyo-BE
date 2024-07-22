package com.econovation.teamponyo.domains.exhibit.query;

import com.econovation.teamponyo.common.enums.ExhibitCategory;
import com.econovation.teamponyo.common.enums.ExhibitStatus;
import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import com.econovation.teamponyo.domains.exhibit.domain.model.QExhibit;
import com.econovation.teamponyo.domains.exhibit.query.port.in.SortStrategies;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesReq;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummaryDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.out.ExhibitQueryDAO;
import com.econovation.teamponyo.infrastructure.s3.S3Uploader;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExhibitQueryAdapter implements ExhibitQueryDAO {
    @PersistenceContext
    private EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;
    private final S3Uploader s3Uploader;

    @Override
    public List<ExhibitSummaryDTO> findSavedExhibits(Long userId, Integer number, Integer pageNumber) {
        String sql = "SELECT exhibit_id FROM saved_exhibits WHERE user_id = :userId LIMIT :limit OFFSET :offset";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("limit", number);
        query.setParameter("offset", (pageNumber - 1) * number);

        List<Long> exhibitIds = query.getResultList();

        QExhibit exhibit = QExhibit.exhibit;
        List<Exhibit> exhibits = jpaQueryFactory.selectFrom(exhibit)
                .where(exhibit.exhibitId.in(exhibitIds))
                .fetch();

        return exhibits.stream()
                .parallel()
                .map(e -> new ExhibitSummaryDTO(
                            e.getExhibitId(),
                            s3Uploader.getPublicUrl(e.getPosterKeyName()),
                            e.getTitle(),
                            e.getPeriod().toString(),
                            e.getExhibitStatus()
                    )
                )
                .toList();
    }

    @Override
    public List<ExhibitSummaryDTO> findVisitedExhibits(Long userId, Integer number, Integer pageNumber) {
        String sql = "SELECT exhibit_id FROM visited_exhibits WHERE user_id = :userId LIMIT :limit OFFSET :offset";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("limit", number);
        query.setParameter("offset", (pageNumber - 1) * number);

        List<Long> exhibitIds = query.getResultList();

        QExhibit exhibit = QExhibit.exhibit;
        List<Exhibit> exhibits = jpaQueryFactory.selectFrom(exhibit)
                .where(exhibit.exhibitId.in(exhibitIds))
                .fetch();

        return exhibits.stream()
                .parallel()
                .map(e -> {
                    return new ExhibitSummaryDTO(
                            e.getExhibitId(),
                            s3Uploader.getPublicUrl(e.getPosterKeyName()),
                            e.getTitle(),
                            e.getPeriod().toString(),
                            e.getExhibitStatus()
                    );
                })
                .toList();
    }

    @Override
    public ExhibitSummariesDTO findExhibitSummaries(ExhibitSummariesReq req) {
        System.out.println("pageNumber:" + req.pageNumber());
        System.out.println("req.number() = " + req.number());

        Pageable pageable = PageRequest.of(req.pageNumber() - 1, req.number());
        QExhibit exhibit = QExhibit.exhibit;

        JPAQuery<Exhibit> query = jpaQueryFactory.selectFrom(exhibit)
                .where(
                        teamIdEq(req.teamId()),
                        exhibitCategoryEq(req.exhibitCategory()),
                        exhibitStatusEq(req.exhibitStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(exhibit, req.sortStrategies());
        if (orderSpecifier != null) query.orderBy(orderSpecifier);

        List<Exhibit> exhibits = query.fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(exhibit.count())
                .from(exhibit)
                .where(
                        teamIdEq(req.teamId()),
                        exhibitCategoryEq(req.exhibitCategory()),
                        exhibitStatusEq(req.exhibitStatus())
                );

        Page<Exhibit> result = PageableExecutionUtils.getPage(exhibits, pageable, countQuery::fetchOne);

        //TODO: 이미지 다운로드 실패 시
        return new ExhibitSummariesDTO(result.getTotalPages(),
                exhibits.stream().parallel()
                    .map(e -> new ExhibitSummaryDTO(
                            e.getExhibitId(),
                            s3Uploader.getPublicUrl(e.getPosterKeyName()),
                            e.getTitle(),
                            e.getPeriod().toString(),
                            e.getExhibitStatus())
                    )
                    .toList());
    }

    private BooleanExpression teamIdEq(Long teamId) {
        return teamId != null ? QExhibit.exhibit.teamId.eq(teamId) : null;
    }

    private BooleanExpression exhibitCategoryEq(ExhibitCategory exhibitCategory) {
        return exhibitCategory != null ? QExhibit.exhibit.exhibitCategory.eq(exhibitCategory) : null;
    }

    private BooleanExpression exhibitStatusEq(ExhibitStatus exhibitStatus) {
        LocalDate now = LocalDate.now();
        if (exhibitStatus == null || exhibitStatus == ExhibitStatus.ALL) {
            return null;
        }
        return switch (exhibitStatus) {
            case BEFORE -> QExhibit.exhibit.period.startDate.gt(now);
            case ONGOING -> QExhibit.exhibit.period.startDate.loe(now)
                    .and(QExhibit.exhibit.period.endDate.goe(now));
            case AFTER -> QExhibit.exhibit.period.endDate.lt(now);
            default -> null;
        };
    }

    private OrderSpecifier<?> getOrderSpecifier(QExhibit exhibit, SortStrategies sortStrategies) {
        if (sortStrategies == SortStrategies.POPULARITY) {
            return new OrderSpecifier<>(Order.DESC, exhibit.viewCount);
        }
        return null;
    }

    @Override
    public boolean isVisitedExhibit(Long userId, Long exhibitId) {
        String sql =
                "SELECT 1 FROM visited_exhibits " +
                        "WHERE user_id = :userId AND exhibit_id = :exhibitId LIMIT 1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("exhibitId", exhibitId);
        return !query.getResultList().isEmpty();
    }

    @Override
    public boolean isSavedExhibit(Long userId, Long exhibitId) {
        String sql =
                "SELECT 1 FROM saved_exhibits " +
                        "WHERE user_id = :userId AND exhibit_id = :exhibitId LIMIT 1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("exhibitId", exhibitId);
        return !query.getResultList().isEmpty();
    }


}
