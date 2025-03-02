package co.kr.cocomu.study.service;

import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.AllStudyCardDto;
import co.kr.cocomu.study.dto.response.LanguageDto;
import co.kr.cocomu.study.dto.response.LeaderDto;
import co.kr.cocomu.study.dto.response.StudyCardDto;
import co.kr.cocomu.study.dto.response.WorkbookDto;
import co.kr.cocomu.study.exception.StudyExceptionCode;
import co.kr.cocomu.study.repository.jpa.LanguageRepository;
import co.kr.cocomu.study.repository.jpa.WorkbookRepository;
import co.kr.cocomu.study.repository.query.StudyQueryRepository;
import co.kr.cocomu.study.repository.query.StudyUserQueryRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyQueryService {

    private final StudyQueryRepository studyQuery;
    private final StudyUserQueryRepository studyUserQuery;
    private final WorkbookRepository workbookQuery;
    private final LanguageRepository languageQuery;

    public AllStudyCardDto getAllStudyCard(final GetAllStudyFilterDto dto, final Long userId) {
        final Long totalStudyCount = studyQuery.countStudyCardsWithFilter(dto, userId);
        final List<StudyCardDto> studyPages = studyQuery.findTop20StudyCardsWithFilter(dto, userId);
        final List<Long> studyIds = studyPages.stream().map(StudyCardDto::getId).toList();
        setStudyInformation(studyIds, studyPages);

        return new AllStudyCardDto(totalStudyCount, studyPages);
    }

    public StudyCardDto getStudyCard(final Long studyId, final Long userId) {
        return studyQuery.findStudyPagesByStudyId(studyId, userId)
            .map(studyPage -> {
                studyPage.setLanguages(languageQuery.findLanguageByStudyId(studyId));
                studyPage.setWorkbooks(workbookQuery.findWorkbookByStudyId(studyId));
                studyPage.setLeader(studyUserQuery.findLeaderByStudyId(studyId));
                return studyPage;
            })
            .orElseThrow(() -> new NotFoundException(StudyExceptionCode.NOT_FOUND_STUDY));
    }

    private void setStudyInformation(final List<Long> studyIds, final List<StudyCardDto> studyPages) {
        final Map<Long, List<LanguageDto>> languageByStudies = languageQuery.findLanguageByStudies(studyIds);
        final Map<Long, List<WorkbookDto>> workbookByStudies = workbookQuery.findWorkbookByStudies(studyIds);
        final Map<Long, LeaderDto> LeaderByStudies = studyUserQuery.findLeaderByStudies(studyIds);

        for (StudyCardDto studyPage: studyPages) {
            studyPage.setLanguages(languageByStudies.getOrDefault(studyPage.getId(), List.of()));
            studyPage.setWorkbooks(workbookByStudies.getOrDefault(studyPage.getId(), List.of()));
            studyPage.setLeader(LeaderByStudies.get(studyPage.getId()));
        }
    }

    public List<WorkbookDto> getAllWorkbooks() {
        final List<Workbook> workbooks = workbookQuery.findAll();
        return workbooks.stream()
            .map(Workbook::toDto)
            .toList();
    }

    public List<LanguageDto> getAllLanguages() {
        final List<Language> languages = languageQuery.findAll();
        return languages.stream()
            .map(Language::toDto)
            .toList();
    }

}
