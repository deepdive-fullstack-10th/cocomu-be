package co.kr.cocomu.codingspace.repository.query.impl;

import static co.kr.cocomu.codingspace.domain.QCodingSpace.codingSpace;
import static co.kr.cocomu.codingspace.domain.QCodingSpaceTab.codingSpaceTab;

import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceDto;
import co.kr.cocomu.codingspace.dto.response.LanguageDto;
import co.kr.cocomu.codingspace.repository.query.CodingSpaceQuery;
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
            .where(
                codingSpace.study.id.eq(studyId),
                getSelectCondition(filter.lastId())
            )
            .orderBy(codingSpace.createdAt.desc(), codingSpace.id.desc())
            .limit(20)
            .fetch();
    }

    private static BooleanExpression getSelectCondition(final Long lastId) {
        if (lastId == null) {
            return null;
        }
        return codingSpace.id.lt(lastId);
    }
    public static BooleanExpression isUserJoined(final Long userId) {
        return JPAExpressions.selectOne()
            .from(codingSpaceTab)
            .where(
                codingSpaceTab.codingSpace.eq(codingSpace),
                codingSpaceTab.user.id.eq(userId)
            )
            .exists();
    }

}
