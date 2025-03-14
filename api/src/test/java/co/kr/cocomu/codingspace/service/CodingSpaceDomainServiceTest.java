package co.kr.cocomu.codingspace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.domain.CodingSpaceTab;
import co.kr.cocomu.codingspace.domain.TestCase;
import co.kr.cocomu.codingspace.domain.vo.TabStatus;
import co.kr.cocomu.codingspace.exception.CodingSpaceExceptionCode;
import co.kr.cocomu.codingspace.repository.CodingSpaceRepository;
import co.kr.cocomu.codingspace.repository.CodingSpaceTabRepository;
import co.kr.cocomu.codingspace.repository.TestCaseRepository;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CodingSpaceDomainServiceTest {

    @Mock private CodingSpaceRepository codingSpaceRepository;
    @Mock private CodingSpaceTabRepository codingSpaceTabRepository;
    @Mock private TestCaseRepository testCaseRepository;

    @InjectMocks private CodingSpaceDomainService codingSpaceDomainService;

    private CodingSpace mockCodingSpace;

    @BeforeEach
    void setUp() {
        mockCodingSpace = mock(CodingSpace.class);
    }

    @Test
    void 코딩_스페이스가_존재한다() {
        // given
        when(codingSpaceRepository.findById(1L)).thenReturn(Optional.of(mockCodingSpace));

        // when
        CodingSpace result = codingSpaceDomainService.getCodingSpaceWithThrow(1L);

        // then
        assertThat(result).isEqualTo(mockCodingSpace);
    }

    @Test
    void 코딩_스페이스가_존재하지_않는다() {
        // given
        when(codingSpaceRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> codingSpaceDomainService.getCodingSpaceWithThrow(1L))
            .isInstanceOf(NotFoundException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.NOT_FOUND_SPACE);
    }

    @Test
    void 이미_코딩_스페이스에_참여했다() {
        // given
        when(codingSpaceTabRepository.existsByUserIdAndCodingSpaceId(1L, 1L)).thenReturn(true);

        // then
        assertThatThrownBy(() -> codingSpaceDomainService.validateCodingSpaceMemberShip(1L, 1L))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.ALREADY_PARTICIPATION_SPACE);
    }

    @Test
    void 코딩_스페이스에_참여하지_않았다() {
        // given
        when(codingSpaceTabRepository.existsByUserIdAndCodingSpaceId(1L, 1L)).thenReturn(false);

        // then
        assertThatCode(() -> codingSpaceDomainService.validateCodingSpaceMemberShip(1L, 1L))
            .doesNotThrowAnyException();
    }

    @Test
    void 코딩_스페이스_탭을_가져온다() {
        // given
        CodingSpaceTab mockTab = mock(CodingSpaceTab.class);
        when(codingSpaceTabRepository.findByUserIdAndCodingSpaceId(1L, 1L)).thenReturn(Optional.of(mockTab));

        // when
        CodingSpaceTab result = codingSpaceDomainService.getCodingSpaceTabWithThrow(1L, 1L);

        // then
        assertThat(result).isEqualTo(mockTab);
    }

    @Test
    void 코딩_스페이스_탭이_없으면_예외가_발생한다() {
        // given
        when(codingSpaceTabRepository.findByUserIdAndCodingSpaceId(1L, 1L)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> codingSpaceDomainService.getCodingSpaceTabWithThrow(1L, 1L))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.NO_PARTICIPATION_SPACE);
    }

    @Test
    void 코딩_스페이스_특정_탭이_실행되고_있다() {
        // given
        when(codingSpaceTabRepository.existsByIdAndStatus(1L, TabStatus.ACTIVE)).thenReturn(true);

        // then
        assertThatCode(() -> codingSpaceDomainService.validateActiveTab(1L)).doesNotThrowAnyException();
    }

    @Test
    void 코딩_스페이스_특정_탭이_실행되지_않고_있다면_예외가_발생한다() {
        // given
        when(codingSpaceTabRepository.existsByIdAndStatus(1L, TabStatus.ACTIVE)).thenReturn(false);

        // then
        assertThatThrownBy(() -> codingSpaceDomainService.validateActiveTab(1L))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.NOT_ACTIVE_TAB);
    }

    @Test
    void 테스트_케이스를_가져온다() {
        // given
        TestCase mockCase = mock(TestCase.class);
        when(testCaseRepository.findById(1L)).thenReturn(Optional.of(mockCase));

        // when
        TestCase result = codingSpaceDomainService.getTestCaseWithThrow(1L);

        // then
        assertThat(result).isEqualTo(mockCase);
    }

    @Test
    void 테스트_케이스가_없으면_예외가_발생한다() {
        // given
        when(testCaseRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> codingSpaceDomainService.getTestCaseWithThrow(1L))
            .isInstanceOf(NotFoundException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.NON_EXISTENT_CASE);
    }

}