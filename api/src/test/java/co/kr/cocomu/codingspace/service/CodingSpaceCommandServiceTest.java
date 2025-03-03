package co.kr.cocomu.codingspace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.dto.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.repository.CodingSpaceRepository;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.service.StudyDomainService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CodingSpaceCommandServiceTest {

    @Mock private StudyDomainService studyDomainService;
    @Mock private CodingSpaceRepository codingSpaceRepository;

    @InjectMocks private CodingSpaceCommandService codingSpaceCommandService;

    @Test
    void 코딩_스페이스를_생성한다() {
        // given
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 2, 0, 1L, "", "", "", List.of());
        Study mockStudy = mock(Study.class);
        CodingSpace mockCodingSpace = mock(CodingSpace.class);
        when(mockCodingSpace.getId()).thenReturn(1L);
        when(studyDomainService.getStudyWithThrow(1L)).thenReturn(mockStudy);
        doNothing().when(studyDomainService).validateStudyMembership(1L, 1L);
        when(codingSpaceRepository.save(any(CodingSpace.class))).thenReturn(mockCodingSpace);

        // when
        Long result = codingSpaceCommandService.createCodingSpace(dto, 1L);

        // then
        assertThat(result).isEqualTo(1L);
    }

}