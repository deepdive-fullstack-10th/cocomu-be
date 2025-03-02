package co.kr.cocomu.study.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class StudyCommandServiceTest {

    @Mock private StudyRepository studyRepository;
    @Mock private WorkbookRepository workbookRepository;
    @Mock private LanguageRepository languageRepository;
    @Mock private StudyUserRepository studyUserRepository;
    @Mock private StudyDomainService studyDomainService;
    @Mock private UserService userService;
    @InjectMocks private StudyCommandService studyCommandService;

    private User mockUser;
    private Study mockStudy;
    private StudyUser mockStudyUser;
    private Workbook mockWorkbook1, mockWorkbook2;
    private Language mockLanguage1, mockLanguage2;
    private CreatePublicStudyDto dto;

    @BeforeEach
    void setUp() {
        mockUser = User.createUser("코코무");
        mockWorkbook1 = Workbook.of("백준", "이미지URL");
        mockWorkbook2 = Workbook.of("프로그래머스", "이미지URL");
        mockLanguage1 = Language.of("자바", "이미지URL");
        mockLanguage2 = Language.of("파이썬", "이미지URL");

        List<Long> judgeIds = List.of(1L, 2L);
        List<Long> languageIds = List.of(1L, 2L);

        dto = new CreatePublicStudyDto("스터디명", languageIds, judgeIds, "설명", 10);

        mockStudy = Study.createPublicStudy(dto);
        ReflectionTestUtils.setField(mockStudy, "id", 1L);

        mockStudyUser = StudyUser.joinStudy(mockStudy, mockUser, StudyRole.LEADER);
        ReflectionTestUtils.setField(mockStudyUser, "id", 1L);
    }

    @Test
    void 공개_스터디방_생성과_리더로_참여에_성공한다() {
        // given
        Long userId = 1L;

        when(workbookRepository.findAllById(dto.workbooks())).thenReturn(List.of(mockWorkbook1, mockWorkbook2));
        when(languageRepository.findAllById(dto.languages())).thenReturn(List.of(mockLanguage1, mockLanguage2));
        when(studyRepository.save(any(Study.class))).thenReturn(mockStudy);
        when(userService.getUserWithThrow(userId)).thenReturn(mockUser);
        when(studyUserRepository.save(any(StudyUser.class))).thenReturn(mockStudyUser);

        // when
        Long result = studyCommandService.createPublicStudy(userId, dto);

        // then
        assertThat(result).isEqualTo(1L);

        // 각 메서드 호출 검증
        verify(workbookRepository).findAllById(dto.workbooks());
        verify(languageRepository).findAllById(dto.languages());
        verify(studyRepository).save(any(Study.class));
        verify(userService).getUserWithThrow(userId);
        verify(studyUserRepository).save(any(StudyUser.class));
    }

    @Test
    void 스터디에_일반_사용자로_참여에_성공한다() {
        // given
        Long userId = 1L;
        Long studyId = 1L;

        doNothing().when(studyDomainService).validateStudyParticipation(userId, studyId);
        when(userService.getUserWithThrow(userId)).thenReturn(mockUser);
        when(studyDomainService.getStudyWithThrow(studyId)).thenReturn(mockStudy);
        when(studyUserRepository.save(any(StudyUser.class))).thenReturn(mockStudyUser);

        // when
        Long result = studyCommandService.joinPublicStudy(userId, studyId);

        // then
        assertThat(result).isEqualTo(1L);
        verify(studyDomainService).validateStudyParticipation(userId, studyId);
        verify(studyDomainService).getStudyWithThrow(studyId);
        verify(userService).getUserWithThrow(userId);
        verify(studyUserRepository).save(any(StudyUser.class));
    }

}