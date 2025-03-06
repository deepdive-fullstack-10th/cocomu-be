package co.kr.cocomu.codingspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceRole;
import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.domain.vo.TabStatus;
import co.kr.cocomu.codingspace.exception.CodingSpaceExceptionCode;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodingSpaceTabTest {

    CodingSpace mockCodingSpace;
    User mockUser;

    @BeforeEach
    void setUp() {
        mockCodingSpace = mock(CodingSpace.class);
        mockUser = mock(User.class);
    }

    @Test
    void 코딩_스페이스가_기본설정으로_생성된다() {
        // given
        // when
        CodingSpaceTab tab = CodingSpaceTab.createMember(mockCodingSpace, mockUser);

        // then
        assertThat(tab.getCodingSpace()).isEqualTo(mockCodingSpace);
        assertThat(tab.getUser()).isEqualTo(mockUser);
        assertThat(tab.getDocumentKey().length()).isEqualTo(36);
        assertThat(tab.getStatus()).isEqualTo(TabStatus.JOIN);
    }

    @Test
    void 코딩_스페이스_탭이_참여자로_생성된다() {
        // given
        // when
        CodingSpaceTab tab = CodingSpaceTab.createMember(mockCodingSpace, mockUser);

        // then
        assertThat(tab.getRole()).isEqualTo(CodingSpaceRole.MEMBER);
    }

    @Test
    void 코딩_스페이스_탭이_방장으로_생성된다() {
        // given
        // when
        CodingSpaceTab tab = CodingSpaceTab.createHost(mockCodingSpace, mockUser);

        // then
        assertThat(tab.getRole()).isEqualTo(CodingSpaceRole.HOST);
    }

    @Test
    void 코딩_스페이스에_입장한다() {
        // given
        when(mockCodingSpace.getStatus()).thenReturn(CodingSpaceStatus.WAITING);
        CodingSpaceTab tab = CodingSpaceTab.createHost(mockCodingSpace, mockUser);

        // when
        tab.enterTab();

        // then
        assertThat(tab.getStatus()).isEqualTo(TabStatus.ACTIVE);
    }

    @Test
    void 코딩_스페이스가_종료되면_입장할_수_없다() {
        // given
        when(mockCodingSpace.getStatus()).thenReturn(CodingSpaceStatus.FINISHED);
        CodingSpaceTab tab = CodingSpaceTab.createHost(mockCodingSpace, mockUser);

        // when & then
        assertThatThrownBy(() -> tab.enterTab())
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.FINISHED_CODING_SPACE);
    }
    @Test
    void 코딩_스페이스에_퇴장한다() {
        // given
        when(mockCodingSpace.getStatus()).thenReturn(CodingSpaceStatus.WAITING);
        CodingSpaceTab tab = CodingSpaceTab.createHost(mockCodingSpace, mockUser);

        // when
        tab.leaveTab();

        // then
        assertThat(tab.getStatus()).isEqualTo(TabStatus.INACTIVE);
    }

    @Test
    void 종료된_코딩_스페이스에서는_퇴장이_없다() {
        // given
        when(mockCodingSpace.getStatus()).thenReturn(CodingSpaceStatus.FINISHED);
        CodingSpaceTab tab = CodingSpaceTab.createHost(mockCodingSpace, mockUser);

        // when & then
        assertThatThrownBy(() -> tab.leaveTab())
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.FINISHED_CODING_SPACE);
    }

    @Test
    void 입장한_상태가_아니다() {
        // given
        CodingSpaceTab tab = CodingSpaceTab.createHost(mockCodingSpace, mockUser);

        // when
        boolean result = tab.isActive();

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 입장한_상태다() {
        // given
        CodingSpaceTab tab = CodingSpaceTab.createHost(mockCodingSpace, mockUser);
        tab.enterTab();

        // when
        boolean result = tab.isActive();

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 방장은_코딩_스페이스를_시작할_수_있다() {
        // given
        CodingSpaceTab tab = CodingSpaceTab.createHost(mockCodingSpace, mockUser);
        tab.enterTab();

        // when
        tab.start();

        // then
        verify(mockCodingSpace).start();
    }

    @Test
    void 입장하지_않았다면_시작_할_수_없다() {
        // given
        CodingSpaceTab tab = CodingSpaceTab.createHost(mockCodingSpace, mockUser);

        // when & then
        assertThatThrownBy(() -> tab.start())
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.NOT_ENTER_SPACE);
    }

    @Test
    void 방장이_아니라면_시작할_수_없다() {
        // given
        CodingSpaceTab tab = CodingSpaceTab.createMember(mockCodingSpace, mockUser);
        tab.enterTab();

        // when & then
        assertThatThrownBy(() -> tab.start())
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", CodingSpaceExceptionCode.MEMBER_CAN_NOT_START);
    }

}