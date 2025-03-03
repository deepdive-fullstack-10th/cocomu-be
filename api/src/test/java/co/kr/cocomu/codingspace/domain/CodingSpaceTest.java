package co.kr.cocomu.codingspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.dto.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.dto.CreateTestCaseDto;
import co.kr.cocomu.codingspace.exception.CodingSpaceExceptionCode;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
import java.util.List;
import org.junit.jupiter.api.Test;

class CodingSpaceTest {

    @Test
    void 코딩_스페이스가_정상적으로_생성된다() {
        // given
        CreateTestCaseDto testCaseDto = new CreateTestCaseDto("input", "output");
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 4, 30, 1L, "", "코딩스페이스", "", List.of(testCaseDto));
        Study mockStudy = mock(Study.class);
        Language mockLanguage = mock(Language.class);

        when(mockStudy.getLanguage(1L)).thenReturn(mockLanguage);

        // when
        CodingSpace codingSpace = CodingSpace.createCodingSpace(dto, mockStudy);

        // then
        assertThat(codingSpace.getName()).isEqualTo("코딩스페이스");
        assertThat(codingSpace.getStatus()).isEqualTo(CodingSpaceStatus.WAITING);
        assertThat(codingSpace.getCurrentUserCount()).isEqualTo(0);
        assertThat(codingSpace.getStudy()).isEqualTo(mockStudy);
        assertThat(codingSpace.getLanguage()).isEqualTo(mockLanguage);
        assertThat(codingSpace.getTestCases()).hasSize(1);
    }

    @Test
    void 코딩_스페이스_최대_인원_초과시_예외가_발생한다() {
        // given
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 5, 30, 1L, "", "코딩스페이스", "", List.of());

        // when & then
        assertThatThrownBy(() -> CodingSpace.createCodingSpace(dto, null))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.MAX_USER_COUNT_IS_FOUR);
    }

    @Test
    void 코딩_스페이스_최소_인원_미만일_시_예외가_발생한다() {
        // given
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 1, 30, 1L, "", "코딩스페이스", "", List.of());

        // when & then
        assertThatThrownBy(() -> CodingSpace.createCodingSpace(dto, null))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.MIN_USER_COUNT_IS_TWO);
    }

}