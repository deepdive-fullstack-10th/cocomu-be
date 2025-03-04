package co.kr.cocomu.codingspace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.dto.response.LanguageDto;
import co.kr.cocomu.study.service.StudyDomainService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CodingSpaceQueryServiceTest {

    @Mock private StudyDomainService studyDomainService;

    @InjectMocks private CodingSpaceQueryService codingSpaceQueryService;

    @Test
    void 스터디에서_사용하는_언어_목록을_가져온다() {
        // given
        Study study = mock(Study.class);
        when(study.getLanguagesDto()).thenReturn(List.of());
        when(studyDomainService.getStudyWithThrow(1L)).thenReturn(study);
        doNothing().when(studyDomainService).validateStudyMembership(1L, 1L);

        // when
        List<LanguageDto> result = codingSpaceQueryService.getStudyLanguages(1L, 1L);

        // then
        assertThat(result).isEmpty();
    }

}