package co.kr.cocomu.codingspace.repository.query.impl;

import static co.kr.cocomu.codingspace.domain.QCodingSpace.codingSpace;
import static co.kr.cocomu.codingspace.domain.QCodingSpaceTab.codingSpaceTab;
import static co.kr.cocomu.codingspace.repository.query.condition.CodingSpaceCondition.getCodingSpaceCondition;
import static co.kr.cocomu.codingspace.repository.query.condition.CodingSpaceCondition.isHost;
import static co.kr.cocomu.codingspace.repository.query.condition.CodingSpaceCondition.isUserJoined;
import static co.kr.cocomu.study.domain.QLanguage.language;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.domain.vo.TabStatus;
import co.kr.cocomu.codingspace.dto.page.StartingPage;
import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceDto;
import co.kr.cocomu.codingspace.dto.response.LanguageDto;
import co.kr.cocomu.codingspace.dto.page.WaitingPage;
import co.kr.cocomu.codingspace.repository.query.CodingSpaceQuery;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
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

    public Optional<WaitingPage> findWaitingPage(final Long codingSpaceId, final Long userId) {
        return Optional.ofNullable(queryFactory
            .select(Projections.fields(
                WaitingPage.class,
                codingSpace.id.as("id"),
                codingSpace.name.as("name"),
                isHost(userId).as("hostMe"),
                codingSpace.description.as("description"),
                codingSpace.workbookUrl.as("workbookUrl"),
                codingSpace.codingMinutes.as("codingMinutes"),
                codingSpace.totalUserCount.as("totalUserCount"),
                codingSpace.status.as("status"),
                Projections.fields(
                    LanguageDto.class,
                    codingSpace.language.id.as("languageId"),
                    codingSpace.language.name.as("languageName"),
                    codingSpace.language.imageUrl.as("languageImageUrl")
                ).as("language")
            ))
            .from(codingSpaceTab)
            .join(codingSpaceTab.codingSpace, codingSpace)
            .join(codingSpace.language, language)
            .where(
                codingSpace.status.eq(CodingSpaceStatus.WAITING),
                codingSpace.id.eq(codingSpaceId),
                codingSpaceTab.status.eq(TabStatus.ACTIVE),
                codingSpaceTab.user.id.eq(userId)
            )
            .fetchOne()
        );
    }

    @Override
    public Optional<StartingPage> findStartingPage(final Long codingSpaceId, final Long userId) {
        return Optional.ofNullable(queryFactory
            .select(Projections.fields(
                StartingPage.class,
                codingSpace.id.as("id"),
                codingSpace.name.as("name"),
                codingSpace.description.as("description"),
                codingSpace.workbookUrl.as("workbookUrl"),
                isHost(userId).as("hostMe"),
                codingSpace.codingMinutes.as("codingMinutes"),
                codingSpace.startTime.as("startTime"),
                codingSpaceTab.id.as("tabId"),
                codingSpaceTab.documentKey.as("documentKey"),
                Projections.fields(
                    LanguageDto.class,
                    codingSpace.language.id.as("languageId"),
                    codingSpace.language.name.as("languageName"),
                    codingSpace.language.imageUrl.as("languageImageUrl")
                ).as("language")
            ))
            .from(codingSpaceTab)
            .join(codingSpaceTab.codingSpace, codingSpace)
            .join(codingSpace.language, language)
            .where(
                codingSpace.status.eq(CodingSpaceStatus.RUNNING),
                codingSpace.id.eq(codingSpaceId),
                codingSpaceTab.status.eq(TabStatus.ACTIVE),
                codingSpaceTab.user.id.eq(userId)
            )
            .fetchOne()
        );
    }

}
