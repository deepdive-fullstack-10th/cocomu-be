package co.kr.cocomu.codingspace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpacesDto;
import co.kr.cocomu.codingspace.repository.CodingSpaceRepository;
import co.kr.cocomu.codingspace.repository.CodingSpaceTabRepository;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.dto.response.LanguageDto;
import co.kr.cocomu.study.service.StudyDomainService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CodingSpaceQueryServiceTest {

    @Mock private StudyDomainService studyDomainService;
    @Mock private CodingSpaceRepository codingSpaceQuery;
    @Mock private CodingSpaceTabRepository codingSpaceTabQuery;

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

    @Test
    void 빈_코딩스페이스_목록을_가져온다() {
        List<CodingSpaceDto> mockSpaces = List.of();
        doNothing().when(studyDomainService).validateStudyMembership(1L, 1L);
        when(codingSpaceQuery.findSpacesWithFilter(anyLong(), anyLong(), any(FilterDto.class))).thenReturn(mockSpaces);
        when(codingSpaceTabQuery.findUsersBySpace(List.of())).thenReturn(Map.of());

        CodingSpacesDto codingSpaces = codingSpaceQueryService.getCodingSpaces(1L, 1L, mock(FilterDto.class));

        assertThat(codingSpaces.codingSpaces()).hasSize(0);
        assertThat(codingSpaces.lastId()).isEqualTo(0L);
    }

    @Test
    void 코딩스페이스_목록을_가져온다() {
        final CodingSpaceDto codingSpaceDto = new CodingSpaceDto();
        codingSpaceDto.setId(1L);
        List<CodingSpaceDto> mockSpaces = List.of(codingSpaceDto);
        doNothing().when(studyDomainService).validateStudyMembership(1L, 1L);
        when(codingSpaceQuery.findSpacesWithFilter(anyLong(), anyLong(), any(FilterDto.class))).thenReturn(mockSpaces);
        when(codingSpaceTabQuery.findUsersBySpace(List.of(1L))).thenReturn(Map.of(1L, List.of()));

        CodingSpacesDto codingSpaces = codingSpaceQueryService.getCodingSpaces(1L, 1L, mock(FilterDto.class));

        assertThat(codingSpaces.codingSpaces()).hasSize(1);
        assertThat(codingSpaces.lastId()).isEqualTo(1L);
    }

}