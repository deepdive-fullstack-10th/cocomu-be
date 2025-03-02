package co.kr.cocomu.study.repository.query;

import static co.kr.cocomu.study.domain.QStudy.study;
import static co.kr.cocomu.study.repository.query.condition.StudyFilterCondition.isUserJoined;

import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.study.controller.query.StudyLanguageQueryRepository;
import co.kr.cocomu.study.controller.query.StudyQueryRepository;
import co.kr.cocomu.study.controller.query.StudyUserQueryRepository;
import co.kr.cocomu.study.controller.query.StudyWorkbookQueryRepository;
import co.kr.cocomu.study.domain.vo.StudyStatus;
import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.AllStudyPageDto;
import co.kr.cocomu.study.dto.response.LanguageDto;
import co.kr.cocomu.study.dto.response.LeaderDto;
import co.kr.cocomu.study.dto.response.StudyPageDto;
import co.kr.cocomu.study.dto.response.WorkbookDto;
import co.kr.cocomu.study.exception.StudyExceptionCode;
import co.kr.cocomu.study.repository.query.condition.StudyFilterCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// todO: DB Access 는 IntegrationTest에서 집중 테스트
@Repository
@RequiredArgsConstructor
public class StudyQueryRepositoryImpl implements StudyQueryRepository {

    private static final int STUDY_PAGE_SIZE = 20;

    private final StudyLanguageQueryRepository languageQuery;
    private final StudyWorkbookQueryRepository workbookQuery;
    private final StudyUserQueryRepository userQuery;
    private final JPAQueryFactory queryFactory;

    public AllStudyPageDto findTop20StudyPagesWithFilter(final GetAllStudyFilterDto filter, final Long userId) {
        final BooleanExpression condition = StudyFilterCondition.buildStudyFilterCondition(filter, userId);

        final Long totalStudyCount = getTotalStudyCount(condition);
        final List<StudyPageDto> studyPages = getStudyPages(filter.page(), userId, condition);
        final List<Long> studyIds = studyPages.stream().map(StudyPageDto::getId).toList();

        final Map<Long, List<LanguageDto>> languageByStudies = languageQuery.findAllByLanguageByStudies(studyIds);
        final Map<Long, List<WorkbookDto>> workbookByStudies = workbookQuery.findAllByWorkbookByStudies(studyIds);
        final Map<Long, LeaderDto> LeaderByStudies = userQuery.findAllLeaderByStudies(studyIds);

        for (StudyPageDto studyPage: studyPages) {
            studyPage.setLanguages(languageByStudies.get(studyPage.getId()));
            studyPage.setWorkbooks(workbookByStudies.get(studyPage.getId()));
            studyPage.setLeader(LeaderByStudies.get(studyPage.getId()));
        }

        return new AllStudyPageDto(totalStudyCount, studyPages);
    }

    public StudyPageDto findStudyPagesByStudyId(final Long studyId, final Long userId) {
        final List<LanguageDto> languages = languageQuery.findLanguageByStudyId(studyId);
        final List<WorkbookDto> workbooks = workbookQuery.findWorkbookByStudyId(studyId);
        final LeaderDto leader = userQuery.findLeaderByStudyId(studyId);

        return getStudyPage(studyId, userId)
            .map(studyPage -> {
                studyPage.setLanguages(languages);
                studyPage.setWorkbooks(workbooks);
                studyPage.setLeader(leader);
                return studyPage;
            })
            .orElseThrow(() -> new NotFoundException(StudyExceptionCode.NOT_FOUND_STUDY));
    }

    private Optional<StudyPageDto> getStudyPage(final Long studyId, final Long userId) {
        return Optional.ofNullable(
                buildStudyPageForm(userId)
                .where(
                    study.id.eq(studyId),
                    study.status.ne(StudyStatus.REMOVE)
                )
                .fetchOne()
        );
    }

    private List<StudyPageDto> getStudyPages(final Long page, final Long userId, final BooleanExpression condition) {
        return buildStudyPageForm(userId)
            .where(condition)
            .orderBy(study.createdAt.desc())
            .offset(pageOffset(page))
            .limit(STUDY_PAGE_SIZE)
            .fetch();
    }

    private JPAQuery<StudyPageDto> buildStudyPageForm(final Long userId) {
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
            .from(study);
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

}
