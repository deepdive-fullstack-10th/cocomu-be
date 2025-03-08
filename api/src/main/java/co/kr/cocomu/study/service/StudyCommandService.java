package co.kr.cocomu.study.service;

import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.domain.StudyUser;
import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.domain.vo.StudyRole;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.repository.jpa.LanguageRepository;
import co.kr.cocomu.study.repository.jpa.StudyRepository;
import co.kr.cocomu.study.repository.jpa.StudyUserRepository;
import co.kr.cocomu.study.repository.jpa.WorkbookRepository;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyCommandService {

    private final StudyDomainService studyDomainService;
    private final UserService userService;
    private final StudyRepository studyRepository;
    private final WorkbookRepository workbookRepository;
    private final LanguageRepository languageRepository;
    private final StudyUserRepository studyUserRepository;

    @Transactional
    public Long createPublicStudy(final Long userId, final CreatePublicStudyDto dto) {
        final User user = userService.getUserWithThrow(userId);

        final Study publicStudy = createPublicStudy(dto);
        final Study savedStudy = studyRepository.save(publicStudy);

        final StudyUser studyUser = StudyUser.joinStudy(savedStudy, user, StudyRole.LEADER);
        studyUserRepository.save(studyUser);

        return savedStudy.getId();
    }

    @Transactional
    public Long joinPublicStudy(final Long userId, final Long studyId) {
        studyDomainService.validateStudyParticipation(userId, studyId);
        final User user = userService.getUserWithThrow(userId);
        final Study study = studyDomainService.getStudyWithThrow(studyId);

        final StudyUser studyUser = StudyUser.joinStudy(study, user, StudyRole.NORMAL);
        studyUserRepository.save(studyUser);

        return studyUser.getStudyId();
    }

    private Study createPublicStudy(final CreatePublicStudyDto dto) {
        final List<Workbook> workbooks = workbookRepository.findAllById(dto.workbooks());
        final List<Language> languages = languageRepository.findAllById(dto.languages());

        // todo: insert 여러번 발생, 책임 분리
        final Study publicStudy = Study.createPublicStudy(dto);
        publicStudy.addBooks(workbooks);
        publicStudy.addLanguages(languages);

        return publicStudy;
    }

}
