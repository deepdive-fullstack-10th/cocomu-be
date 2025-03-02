package co.kr.cocomu.study.repository.query;

import static co.kr.cocomu.study.domain.QLanguage.language;
import static co.kr.cocomu.study.domain.QStudy.study;
import static co.kr.cocomu.study.domain.QStudyLanguage.studyLanguage;
import static co.kr.cocomu.study.domain.QStudyUser.studyUser;
import static co.kr.cocomu.study.domain.QStudyWorkbook.studyWorkbook;
import static co.kr.cocomu.study.domain.QWorkbook.workbook;
import static co.kr.cocomu.study.repository.query.condition.StudyFilterCondition.isUserJoined;
import static co.kr.cocomu.user.domain.QUser.user;

import co.kr.cocomu.study.controller.query.StudyQueryRepository;
import co.kr.cocomu.study.domain.vo.StudyRole;
import co.kr.cocomu.study.domain.vo.StudyUserStatus;
import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.AllStudyPageDto;
import co.kr.cocomu.study.dto.response.LanguageDto;
import co.kr.cocomu.study.dto.response.LeaderDto;
import co.kr.cocomu.study.dto.response.StudyPageDto;
import co.kr.cocomu.study.dto.response.WorkbookDto;
import co.kr.cocomu.study.repository.query.condition.StudyFilterCondition;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// todO: DB Access 는 IntegrationTest에서 집중 테스트
@Repository
@RequiredArgsConstructor
public class StudyQueryRepositoryImpl implements StudyQueryRepository {

    private static final int STUDY_PAGE_SIZE = 20;

    private final JPAQueryFactory queryFactory;

    public AllStudyPageDto findTop20StudyPagesWithFilter(final GetAllStudyFilterDto filter, final Long userId) {
        final BooleanExpression condition = StudyFilterCondition.buildStudyFilterCondition(filter, userId);

        final Long totalStudyCount = getTotalStudyCount(condition);
        final List<StudyPageDto> studyPages = getStudyPages(filter.page(), userId, condition);
        final List<Long> studyIds = studyPages.stream().map(StudyPageDto::getId).toList();

        final Map<Long, List<LanguageDto>> allLanguageByStudies = findAllByLanguageByStudies(studyIds);
        final Map<Long, List<WorkbookDto>> allWorkbookByStudies = findAllByWorkbookByStudies(studyIds);
        final Map<Long, LeaderDto> allLeaderByStudies = findAllLeaderByStudies(studyIds);

        for (StudyPageDto studyPage: studyPages) {
            studyPage.setLanguages(allLanguageByStudies.get(studyPage.getId()));
            studyPage.setWorkbooks(allWorkbookByStudies.get(studyPage.getId()));
            studyPage.setLeader(allLeaderByStudies.get(studyPage.getId()));
        }

        return new AllStudyPageDto(totalStudyCount, studyPages);
    }

    private List<StudyPageDto> getStudyPages(final Long page, final Long userId, final BooleanExpression condition) {
        return queryFactory.select(
                Projections.fields(
                    StudyPageDto.class,
                    study.id.as("id"),
                    study.name.as("name"),
                    study.status.as("status"),
                    study.description.as("description"),
                    study.currentUserCount.as("currentUserCount"),
                    study.totalUserCount.as("totalUserCount"),
                    study.createdAt.as("createdAt"),
                    isUserJoined(userId).as("joinable")
                )
            )
            .from(study)
            .where(condition)
            .orderBy(study.createdAt.desc())
            .offset(pageOffset(page))
            .limit(STUDY_PAGE_SIZE)
            .fetch();
    }

    private long pageOffset(final Long page) {
        if (page == null || page <= 0) {
            return STUDY_PAGE_SIZE;
        }
        return (page - 1) * STUDY_PAGE_SIZE;
    }

    private Long getTotalStudyCount(final BooleanExpression condition) {
        return queryFactory.select(study.count())
            .from(study)
            .where(condition)
            .fetchOne();
    }

    private Map<Long, List<LanguageDto>> findAllByLanguageByStudies(final List<Long> studyIds) {
        return queryFactory.select(studyLanguage.study.id, language.name)
            .from(studyLanguage)
            .join(language).on(studyLanguage.language.id.eq(language.id))
            .where(studyLanguage.study.id.in(studyIds))
            .transform(GroupBy.groupBy(studyLanguage.study.id)
                .as(GroupBy.list(
                    Projections.fields(
                        LanguageDto.class,
                        language.id.as("id"),
                        language.name.as("name"),
                        language.imageUrl.as("imageUrl")
                    )
                ))
            );
    }

    private Map<Long, List<WorkbookDto>> findAllByWorkbookByStudies(final List<Long> studyIds) {
        return queryFactory.select(studyWorkbook.study.id, workbook)
            .from(studyWorkbook)
            .join(workbook).on(studyWorkbook.workbook.id.eq(workbook.id))
            .where(studyWorkbook.study.id.in(studyIds))
            .transform(GroupBy.groupBy(studyWorkbook.study.id)
                .as(GroupBy.list(
                    Projections.fields(
                        WorkbookDto.class,
                        workbook.id.as("id"),
                        workbook.name.as("name"),
                        workbook.imageUrl.as("imageUrl")
                    )
                ))
            );
    }

    private Map<Long, LeaderDto> findAllLeaderByStudies(final List<Long> studyIds) {
        return queryFactory.select(studyUser.study.id, user)
            .from(studyUser)
            .join(user).on(studyUser.user.id.eq(user.id))
            .where(
                studyUser.study.id.in(studyIds),
                studyUser.studyRole.eq(StudyRole.LEADER),
                studyUser.studyUserStatus.eq(StudyUserStatus.JOIN)
            )
            .transform(GroupBy.groupBy(studyUser.study.id).as(
                Projections.fields(
                    LeaderDto.class,
                    user.id.as("id"),
                    user.nickname.as("nickname"),
                    user.profileImageUrl.as("profileImageUrl")
                )
            ));
    }

}
