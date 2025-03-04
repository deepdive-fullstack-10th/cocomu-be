package co.kr.cocomu.codingspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.dto.request.CreateTestCaseDto;
import co.kr.cocomu.codingspace.exception.CodingSpaceExceptionCode;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodingSpaceTest {

    Study mockStudy;
    User mockUser;
    User otherUser;
    Language mockLanguage;

    @BeforeEach
    void setUp() {
        mockStudy = mock(Study.class);
        mockLanguage = mock(Language.class);
        mockUser = mock(User.class);
        otherUser = mock(User.class);
    }


    @Test
    void 코딩_스페이스가_정상적으로_생성된다() {
        // given
        CreateTestCaseDto testCaseDto = new CreateTestCaseDto("input", "output");
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 4, 30, 1L, "", "코딩스페이스", "", List.of(testCaseDto));
        when(mockStudy.getLanguage(1L)).thenReturn(mockLanguage);

        // when
        CodingSpace codingSpace = CodingSpace.createCodingSpace(dto, mockStudy, mockUser);

        // then
        assertThat(codingSpace.getName()).isEqualTo("코딩스페이스");
        assertThat(codingSpace.getStatus()).isEqualTo(CodingSpaceStatus.WAITING);
        assertThat(codingSpace.getStudy()).isEqualTo(mockStudy);
        assertThat(codingSpace.getLanguage()).isEqualTo(mockLanguage);
        assertThat(codingSpace.getTestCases()).hasSize(1);
    }

    @Test
    void 코딩_스페이스가_생성시_방장이_추가된다() {
        // given
        CreateTestCaseDto testCaseDto = new CreateTestCaseDto("input", "output");
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 4, 30, 1L, "", "코딩스페이스", "", List.of(testCaseDto));
        when(mockStudy.getLanguage(1L)).thenReturn(mockLanguage);

        // when
        CodingSpace codingSpace = CodingSpace.createCodingSpace(dto, mockStudy, mockUser);

        // then
        assertThat(codingSpace.getCurrentUserCount()).isEqualTo(1);
        assertThat(codingSpace.getTabs()).hasSize(1);
    }

    @Test
    void 코딩_스페이스_최대_인원_초과시_예외가_발생한다() {
        // given
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 5, 30, 1L, "", "코딩스페이스", "", List.of());

        // when & then
        assertThatThrownBy(() -> CodingSpace.createCodingSpace(dto, null, null))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.MAX_USER_COUNT_IS_FOUR);
    }

    @Test
    void 코딩_스페이스_최소_인원_미만일_시_예외가_발생한다() {
        // given
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 1, 30, 1L, "", "코딩스페이스", "", List.of());

        // when & then
        assertThatThrownBy(() -> CodingSpace.createCodingSpace(dto, null, null))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.MIN_USER_COUNT_IS_TWO);
    }

    @Test
    void 코딩_테스트에_참여가_된다() {
        // given
        CreateTestCaseDto testCaseDto = new CreateTestCaseDto("input", "output");
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 2, 30, 1L, "", "코딩스페이스", "", List.of(testCaseDto));
        when(mockStudy.getLanguage(1L)).thenReturn(mockLanguage);

        CodingSpace codingSpace = CodingSpace.createCodingSpace(dto, mockStudy, mockUser);
        int currentCount = codingSpace.getCurrentUserCount();

        // when
        codingSpace.joinUser(otherUser);

        // then
        assertThat(codingSpace.getCurrentUserCount()).isEqualTo(currentCount + 1);
        assertThat(codingSpace.getTabs()).hasSize(2);
    }

    @Test
    void 코딩_테스트에_이미_참여한_경우_예외가_발생한다() {
        // given
        CreateTestCaseDto testCaseDto = new CreateTestCaseDto("input", "output");
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 2, 30, 1L, "", "코딩스페이스", "", List.of(testCaseDto));
        when(mockStudy.getLanguage(1L)).thenReturn(mockLanguage);

        CodingSpace codingSpace = CodingSpace.createCodingSpace(dto, mockStudy, mockUser);
        codingSpace.joinUser(otherUser);

        // when & then
        assertThatThrownBy(() -> codingSpace.joinUser(otherUser))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.ALREADY_PARTICIPATION_SPACE);
    }

    void 코딩_테스트가_대기중이_아닐_경우_참여하면_예외가_발생한다() {
        // todo: 코드 시작 및 종료 등을 작업 후 테스트
    }

    @Test
    void 코딩_테스트가_참여_시_최대_인원을_초과하는_경우_예외가_발생한다() {
        // given
        CreateTestCaseDto testCaseDto = new CreateTestCaseDto("input", "output");
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 2, 30, 1L, "", "코딩스페이스", "", List.of(testCaseDto));
        when(mockStudy.getLanguage(1L)).thenReturn(mockLanguage);

        CodingSpace codingSpace = CodingSpace.createCodingSpace(dto, mockStudy, mockUser);
        codingSpace.joinUser(otherUser);
        User otherUser2 = mock(User.class);

        // when & then
        assertThatThrownBy(() -> codingSpace.joinUser(otherUser2))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.OVER_USER_COUNT);
    }

}