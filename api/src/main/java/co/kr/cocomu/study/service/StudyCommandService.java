package co.kr.cocomu.study.service;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.dto.request.CreatePrivateStudyDto;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.exception.StudyExceptionCode;
import co.kr.cocomu.study.repository.jpa.LanguageRepository;
import co.kr.cocomu.study.repository.jpa.StudyRepository;
import co.kr.cocomu.study.repository.jpa.WorkbookRepository;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public Long createPublicStudy(final Long userId, final CreatePublicStudyDto dto) {
        final User user = userService.getUserWithThrow(userId);
        final List<Workbook> workbooks = workbookRepository.findAllById(dto.workbooks());
        final List<Language> languages = languageRepository.findAllById(dto.languages());

        final Study study = Study.createPublicStudy(dto);
        study.joinLeader(user);
        study.addLanguages(languages);
        study.addWorkBooks(workbooks);

        return studyRepository.save(study).getId();
    }

    @Transactional
    public Long joinPublicStudy(final Long userId, final Long studyId) {
        studyDomainService.validateStudyParticipation(userId, studyId);
        final User user = userService.getUserWithThrow(userId);
        final Study study = studyDomainService.getStudyWithThrow(studyId);
        study.joinMember(user);

        return study.getId();
    }

    public Long createPrivateStudy(final CreatePrivateStudyDto dto, final Long userId) {
        final User user = userService.getUserWithThrow(userId);
        final List<Workbook> workbooks = workbookRepository.findAllById(dto.workbooks());
        final List<Language> languages = languageRepository.findAllById(dto.languages());

        final String encodedPassword = passwordEncoder.encode(dto.password());
        final Study study = Study.createPrivateStudy(dto, encodedPassword);
        study.joinLeader(user);
        study.addLanguages(languages);
        study.addWorkBooks(workbooks);

        return studyRepository.save(study).getId();
    }

    public Long joinPrivateStudy(final Long userId, final Long studyId, final String password) {
        studyDomainService.validateStudyParticipation(userId, studyId);
        final User user = userService.getUserWithThrow(userId);
        final Study study = studyDomainService.getStudyWithThrow(studyId);
        validatePrivateStudyPassword(password, study.getPassword());
        study.joinMember(user);

        return study.getId();
    }

    private void validatePrivateStudyPassword(final String password, final String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new BadRequestException(StudyExceptionCode.STUDY_PASSWORD_WRONG);
        }
    }

}
