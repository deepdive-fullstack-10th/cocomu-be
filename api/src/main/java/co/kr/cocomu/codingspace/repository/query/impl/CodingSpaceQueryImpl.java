package co.kr.cocomu.codingspace.repository.query.impl;

import static co.kr.cocomu.codingspace.domain.QCodingSpace.codingSpace;
import static co.kr.cocomu.codingspace.domain.QCodingSpaceTab.codingSpaceTab;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceDto;
import co.kr.cocomu.codingspace.dto.response.LanguageDto;
import co.kr.cocomu.codingspace.repository.query.CodingSpaceQuery;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CodingSpaceQueryImpl implements CodingSpaceQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CodingSpaceDto> findSpacesWithFilter(
        final Long userId,
        final Long studyId,
        final FilterDto filter
    ) {
        return queryFactory
            .select(
                Projections.fields(
                    CodingSpaceDto.class,
                    codingSpace.id.as("id"),
                    isUserJoined(userId).as("joinedMe"),
                    codingSpace.name.as("name"),
                    Projections.fields(
                        LanguageDto.class,
                        codingSpace.language.id.as("languageId"),
                        codingSpace.language.name.as("languageName"),
                        codingSpace.language.imageUrl.as("languageImageUrl")
                    ).as("language"),
                    codingSpace.currentUserCount.as("currentUserCount"),
                    codingSpace.totalUserCount.as("totalUserCount"),
                    codingSpace.createdAt.as("createdAt"),
                    codingSpace.status.as("status")
                )
            )
            .from(codingSpace)
            .where(getCodingSpaceCondition(filter, userId, studyId))
            .orderBy(codingSpace.createdAt.desc(), codingSpace.id.desc())
            .limit(20)
            .fetch();
    }

    public Predicate[] getCodingSpaceCondition(final FilterDto filter, final Long userId, final Long studyId) {
        if (filter == null) {
            return new Predicate[] { codingSpace.study.id.eq(studyId) };
        }

        return new Predicate[] {
            codingSpace.study.id.eq(studyId),
            getSelectCondition(filter.lastId()),
            getStatusCondition(filter.status()),
            getJoinableCondition(filter.joinable(), userId),
            getSearchCondition(filter.keyword()),
            getLanguageCondition(filter.languageIds())
        };
    }

    private static BooleanExpression getStatusCondition(final CodingSpaceStatus status) {
        if (status == null) {
            return null;
        }
        return codingSpace.status.eq(status);
    }

    private static BooleanExpression getSelectCondition(final Long lastId) {
        if (lastId == null) {
            return null;
        }
        return codingSpace.id.lt(lastId);
    }

    private static BooleanExpression isUserJoined(final Long userId) {
        return JPAExpressions.selectOne()
            .from(codingSpaceTab)
            .where(
                codingSpaceTab.codingSpace.eq(codingSpace),
                codingSpaceTab.user.id.eq(userId)
            )
            .exists();
    }

    private BooleanExpression getJoinableCondition(final Boolean joinable, final Long userId) {
        if (joinable == null || !joinable) {
            return null;
        }

        return codingSpace.currentUserCount.lt(codingSpace.totalUserCount)
            .and(codingSpace.status.eq(CodingSpaceStatus.WAITING))
            .and(userNotJoinedCondition(userId));
    }

    private BooleanExpression userNotJoinedCondition(final Long userId) {
        return JPAExpressions.selectOne()
            .from(codingSpaceTab)
            .where(
                codingSpaceTab.codingSpace.eq(codingSpace),
                codingSpaceTab.user.id.eq(userId)
            )
            .notExists();
    }
    private BooleanExpression getSearchCondition(final String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        return codingSpace.name.containsIgnoreCase(keyword);
    }

    private BooleanExpression getLanguageCondition(final List<Long> languageIds) {
        if (languageIds == null) {
            return null;
        }

        return codingSpace.language.id.in(languageIds);
    }

}
