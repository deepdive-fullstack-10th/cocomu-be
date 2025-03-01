package co.kr.cocomu.study.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.repository.LanguageJpaRepository;
import co.kr.cocomu.study.repository.StudyJpaRepository;
import co.kr.cocomu.study.repository.WorkbookJpaRepository;
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
class StudyServiceTest {

    @Mock private StudyJpaRepository studyJpaRepository;
    @Mock private WorkbookJpaRepository workbookJpaRepository;
    @Mock private LanguageJpaRepository languageJpaRepository;
    @Mock private UserService userService;
    @InjectMocks private StudyService studyService;

    private User mockUser;
    private Study mockStudy;
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

        mockStudy = Study.createPublicStudy(mockUser, dto);
        ReflectionTestUtils.setField(mockStudy, "id", 1L);
    }

    @Test
    void 공개_스터디방_생성에_성공한다() {
        // given
        Long userId = 1L;

        when(userService.getUserWithThrow(userId)).thenReturn(mockUser);
        when(workbookJpaRepository.findAllById(dto.workbooks())).thenReturn(List.of(mockWorkbook1, mockWorkbook2));
        when(languageJpaRepository.findAllById(dto.languages())).thenReturn(List.of(mockLanguage1, mockLanguage2));
        when(studyJpaRepository.save(any(Study.class))).thenReturn(mockStudy);

        // when
        Long result = studyService.createPublicStudy(userId, dto);

        // then
        assertThat(result).isEqualTo(1L);
        verify(userService).getUserWithThrow(userId);
        verify(workbookJpaRepository).findAllById(dto.workbooks());
        verify(languageJpaRepository).findAllById(dto.languages());
        verify(studyJpaRepository).save(any(Study.class));
    }

}