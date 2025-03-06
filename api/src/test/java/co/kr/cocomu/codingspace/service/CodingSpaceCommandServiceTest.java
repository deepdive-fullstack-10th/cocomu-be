package co.kr.cocomu.codingspace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.domain.CodingSpaceTab;
import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.stomp.StompSSEProducer;
import co.kr.cocomu.codingspace.repository.CodingSpaceRepository;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.service.StudyDomainService;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CodingSpaceCommandServiceTest {

    @Mock private StudyDomainService studyDomainService;
    @Mock private CodingSpaceRepository codingSpaceRepository;
    @Mock private CodingSpaceDomainService codingSpaceDomainService;
    @Mock private StompSSEProducer stompSSEProducer;
    @Mock private UserService userService;

    @InjectMocks private CodingSpaceCommandService codingSpaceCommandService;

    Study mockStudy;
    User mockUser;
    CodingSpace mockCodingSpace;

    @BeforeEach
    void setUp() {
        mockStudy = mock(Study.class);
        mockUser = mock(User.class);
        mockCodingSpace = mock(CodingSpace.class);
    }

    @Test
    void 코딩_스페이스를_생성한다() {
        // given
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 2, 0, 1L, "", "", "", List.of());
        코딩_스페이스_생성_스텁();

        // when
        Long result = codingSpaceCommandService.createCodingSpace(dto, 1L);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    void 코딩_스페이스에_참여한다() {
        // given
        코딩_스페이스_참여_스텁();

        // when
        Long result = codingSpaceCommandService.joinCodingSpace(1L, 1L);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    void 대기방_입장을_한다() {
        // given
        CodingSpaceTab mockTab = mock(CodingSpaceTab.class);
        when(mockTab.getUser()).thenReturn(mockUser);
        when(mockTab.getCodingSpace()).thenReturn(mockCodingSpace);
        when(mockCodingSpace.getStatus()).thenReturn(CodingSpaceStatus.WAITING);
        when(codingSpaceDomainService.getCodingSpaceTabWithThrow(1L, 1L)).thenReturn(mockTab);
        doNothing().when(mockTab).enterTab();
        doNothing().when(stompSSEProducer).publishUserEnter(mockUser, 1L);

        // when
        CodingSpaceStatus status = codingSpaceCommandService.enterSpace(1L, 1L);

        // then
        assertThat(status).isEqualTo(CodingSpaceStatus.WAITING);
    }

    @Test
    void 코딩스페이스_퇴장을_한다() {
        // given
        CodingSpaceTab mockTab = mock(CodingSpaceTab.class);
        when(mockTab.getUser()).thenReturn(mockUser);
        when(codingSpaceDomainService.getCodingSpaceTabWithThrow(1L, 1L)).thenReturn(mockTab);
        doNothing().when(mockTab).leaveTab();
        doNothing().when(stompSSEProducer).publishUserLeave(mockUser, 1L);

        // when
        codingSpaceCommandService.leaveSpace(1L, 1L);

        // then
        verify(stompSSEProducer).publishUserLeave(mockUser, 1L);
    }

    @Test
    void 코딩_스페이스_시작을_한다() {
        // given
        CodingSpaceTab mockTab = mock(CodingSpaceTab.class);
        when(codingSpaceDomainService.getCodingSpaceTabWithThrow(1L, 1L)).thenReturn(mockTab);
        doNothing().when(mockTab).start();
        doNothing().when(stompSSEProducer).publishStartSpace(1L);

        // when
        codingSpaceCommandService.startSpace(1L, 1L);

        // then
        verify(stompSSEProducer).publishStartSpace(1L);
    }

    /*
    * ========================== SET STUB ==========================
    * */

    private void 코딩_스페이스_생성_스텁() {
        when(mockStudy.getId()).thenReturn(1L);
        when(mockUser.getId()).thenReturn(1L);
        when(mockCodingSpace.getId()).thenReturn(1L);

        when(studyDomainService.getStudyWithThrow(1L)).thenReturn(mockStudy);
        when(userService.getUserWithThrow(1L)).thenReturn(mockUser);
        doNothing().when(studyDomainService).validateStudyMembership(1L, 1L);
        when(codingSpaceRepository.save(any(CodingSpace.class))).thenReturn(mockCodingSpace);
    }

    private void 코딩_스페이스_참여_스텁() {
        when(mockStudy.getId()).thenReturn(1L);
        when(mockUser.getId()).thenReturn(1L);
        when(mockCodingSpace.getStudy()).thenReturn(mockStudy);
        doNothing().when(mockCodingSpace).joinUser(mockUser);
        when(mockCodingSpace.getId()).thenReturn(1L);

        when(codingSpaceDomainService.getCodingSpaceWithThrow(1L)).thenReturn(mockCodingSpace);
        when(userService.getUserWithThrow(1L)).thenReturn(mockUser);
        doNothing().when(studyDomainService).validateStudyMembership(1L, 1L);
    }

}