package co.kr.cocomu.codingspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceRole;
import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.domain.vo.TabStatus;
import co.kr.cocomu.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodingSpaceTabTest {

    CodingSpace mockCodingSpace;
    User mockUser;

    @BeforeEach
    void setUp() {
        CodingSpace mockCodingSpace = mock(CodingSpace.class);
        User mockUser = mock(User.class);
    }

    @Test
    void 코딩_스페이스가_기본설정으로_생성된다() {
        // given
        // when
        CodingSpaceTab tab = CodingSpaceTab.createMember(mockCodingSpace, mockUser);

        // then
        assertThat(tab.getCodingSpace()).isEqualTo(mockCodingSpace);
        assertThat(tab.getUser()).isEqualTo(mockUser);
        assertThat(tab.getId().length()).isEqualTo(36);
        assertThat(tab.getStatus()).isEqualTo(TabStatus.ACTIVE);
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

}