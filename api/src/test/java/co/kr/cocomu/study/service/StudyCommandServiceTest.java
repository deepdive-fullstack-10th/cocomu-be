package co.kr.cocomu.study.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.repository.jpa.LanguageRepository;
import co.kr.cocomu.study.repository.jpa.StudyRepository;
import co.kr.cocomu.study.repository.jpa.StudyUserRepository;
import co.kr.cocomu.study.repository.jpa.WorkbookRepository;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.service.UserService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyCommandServiceTest {

    @Mock private StudyRepository studyRepository;
    @Mock private WorkbookRepository workbookRepository;
    @Mock private LanguageRepository languageRepository;
    @Mock private StudyDomainService studyDomainService;
    @Mock private UserService userService;
    @InjectMocks private StudyCommandService studyCommandService;

    @Test
    void 공개_스터디방_생성과_리더로_참여에_성공한다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("스터디명", List.of(), List.of(), "설명", 10);
        Study mockStudy = mock(Study.class);
        User mockUser = mock(User.class);

        when(workbookRepository.findAllById(dto.workbooks())).thenReturn(List.of());
        when(languageRepository.findAllById(dto.languages())).thenReturn(List.of());
        when(userService.getUserWithThrow(1L)).thenReturn(mockUser);
        when(mockStudy.getId()).thenReturn(1L);
        when(studyRepository.save(any(Study.class))).thenReturn(mockStudy);

        // when
        Long result = studyCommandService.createPublicStudy(1L, dto);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    void 스터디에_일반_사용자로_참여에_성공한다() {
        // given
        Study mockStudy = mock(Study.class);
        User mockUser = mock(User.class);
        doNothing().when(studyDomainService).validateStudyParticipation(1L, 1L);
        when(userService.getUserWithThrow(1L)).thenReturn(mockUser);
        when(studyDomainService.getStudyWithThrow(1L)).thenReturn(mockStudy);
        when(mockStudy.getId()).thenReturn(1L);

        // when
        Long result = studyCommandService.joinPublicStudy(1L, 1L);

        // then
        assertThat(result).isEqualTo(1L);
    }

}