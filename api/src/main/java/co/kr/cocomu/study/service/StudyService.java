package co.kr.cocomu.study.service;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.domain.StudyUser;
import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.domain.vo.StudyRole;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.exception.StudyExceptionCode;
import co.kr.cocomu.study.repository.LanguageJpaRepository;
import co.kr.cocomu.study.repository.StudyJpaRepository;
import co.kr.cocomu.study.repository.StudyUserJpaRepository;
import co.kr.cocomu.study.repository.WorkbookJpaRepository;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyJpaRepository studyJpaRepository;
    private final WorkbookJpaRepository workbookJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;
    private final StudyUserJpaRepository studyUserJpaRepository;
    private final UserService userService;

    public Long createPublicStudy(final Long userId, final CreatePublicStudyDto dto) {
        final List<Workbook> workbooks = workbookJpaRepository.findAllById(dto.workbooks());
        final List<Language> languages = languageJpaRepository.findAllById(dto.languages());

        final Study publicStudy = Study.createPublicStudy(dto);
        publicStudy.addBooks(workbooks);
        publicStudy.addLanguages(languages);

        final Study saveStudy = studyJpaRepository.save(publicStudy);
        return joinPublicStudy(userId, saveStudy.getId(), StudyRole.LEADER);
    }

    public Long joinPublicStudy(final Long userId, final Long studyId, final StudyRole studyRole) {
        validateStudyUser(userId, studyId);

        final User user = userService.getUserWithThrow(userId);
        final Study study = getStudyWithThrow(studyId);
        final StudyUser studyUser = StudyUser.joinStudy(study, user, studyRole);
        studyUserJpaRepository.save(studyUser);

        return studyUser.getStudyId();
    }

    private Study getStudyWithThrow(final Long studyId) {
        return studyJpaRepository.findById(studyId)
            .orElseThrow(() -> new NotFoundException(StudyExceptionCode.NOT_FOUND_STUDY));
    }

    private void validateStudyUser(final Long userId, final Long studyId) {
        if (studyUserJpaRepository.existsByUser_IdAndStudy_Id(userId, studyId)) {
            throw new BadRequestException(StudyExceptionCode.ALREADY_PARTICIPATION_STUDY);
        }
    }

}
