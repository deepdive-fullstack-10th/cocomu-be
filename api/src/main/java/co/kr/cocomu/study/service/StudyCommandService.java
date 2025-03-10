package co.kr.cocomu.study.service;

import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.domain.StudyUser;
import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.dto.request.CreatePrivateStudyDto;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.dto.request.EditStudyDto;
import co.kr.cocomu.study.repository.jpa.LanguageRepository;
import co.kr.cocomu.study.repository.jpa.StudyRepository;
import co.kr.cocomu.study.repository.jpa.WorkbookRepository;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudyCommandService {

    private final StudyDomainService studyDomainService;
    private final UserService userService;
    private final StudyPasswordService studyPasswordService;
    private final StudyRepository studyRepository;
    private final WorkbookRepository workbookRepository;
    private final LanguageRepository languageRepository;

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

    public Long joinPublicStudy(final Long userId, final Long studyId) {
        final User user = userService.getUserWithThrow(userId);
        final Study study = studyDomainService.getStudyWithThrow(studyId);
        study.joinPublicMember(user);

        return study.getId();
    }

    public Long createPrivateStudy(final CreatePrivateStudyDto dto, final Long userId) {
        final User user = userService.getUserWithThrow(userId);
        final List<Workbook> workbooks = workbookRepository.findAllById(dto.workbooks());
        final List<Language> languages = languageRepository.findAllById(dto.languages());

        final String encodedPassword = studyPasswordService.encodeStudyPassword(dto.password());
        final Study study = Study.createPrivateStudy(dto, encodedPassword);
        study.addLanguages(languages);
        study.addWorkBooks(workbooks);
        study.joinLeader(user);

        return studyRepository.save(study).getId();
    }

    public Long joinPrivateStudy(final Long userId, final Long studyId, final String password) {
        final User user = userService.getUserWithThrow(userId);
        final Study study = studyDomainService.getStudyWithThrow(studyId);
        studyPasswordService.validatePrivateStudyPassword(password, study.getPassword());
        study.joinPrivateMember(user);

        return study.getId();
    }

    public void leaveStudy(final Long userId, final Long studyId) {
        final StudyUser studyUser = studyDomainService.getStudyUserWithThrow(studyId, userId);
        studyUser.leaveStudy();
    }

    public void removeStudy(final Long userId, final Long studyId) {
        final StudyUser studyUser = studyDomainService.getStudyUserWithThrow(studyId, userId);
        studyUser.removeStudy();
    }

    public Long editPublicStudy(final Long studyId, final Long userId, final EditStudyDto dto) {
        final StudyUser studyUser = studyDomainService.getStudyUserWithThrow(studyId, userId);
        final List<Workbook> workbooks = workbookRepository.findAllById(dto.workbooks());
        final List<Language> languages = languageRepository.findAllById(dto.languages());
        studyUser.editPublicStudy(dto, workbooks, languages);

        return studyUser.getStudyId();
    }

    public Long editPrivateStudy(final Long studyId, final Long userId, final EditStudyDto dto) {
        final StudyUser studyUser = studyDomainService.getStudyUserWithThrow(studyId, userId);
        final List<Workbook> workbooks = workbookRepository.findAllById(dto.workbooks());
        final List<Language> languages = languageRepository.findAllById(dto.languages());
        final String encodedPassword = studyPasswordService.encodeStudyPassword(dto.password());
        studyUser.editPrivateStudy(dto, workbooks, languages, encodedPassword);

        return studyUser.getStudyId();
    }

}
