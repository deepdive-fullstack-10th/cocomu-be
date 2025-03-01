package co.kr.cocomu.study.service;

import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.repository.WorkbookJpaRepository;
import co.kr.cocomu.study.repository.LanguageJpaRepository;
import co.kr.cocomu.study.repository.StudyJpaRepository;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyJpaRepository studyJpaRepository;
    private final WorkbookJpaRepository workbookJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;
    private final UserService userService;

    public Long createPublicStudy(final Long userId, final CreatePublicStudyDto dto) {
        final User user = userService.getUserWithThrow(userId);
        final List<Workbook> workbooks = workbookJpaRepository.findAllById(dto.workbooks());
        final List<Language> languages = languageJpaRepository.findAllById(dto.languages());

        final Study publicStudy = Study.createPublicStudy(user, dto);
        publicStudy.addBooks(workbooks);
        publicStudy.addLanguages(languages);

        final Study saveStudy = studyJpaRepository.save(publicStudy);
        return saveStudy.getId();
    }

}
