package co.kr.cocomu.codingspace.stomp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import co.kr.cocomu.codingspace.dto.message.EventMessage;
import co.kr.cocomu.codingspace.dto.response.TestCaseDto;
import co.kr.cocomu.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@ExtendWith(MockitoExtension.class)
class StompSSEProducerTest {

    @Mock private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks private StompSSEProducer stompSSEProducer;

    @Test
    void 사용자_입장_알림이_발행된다() {
        // given
        User mockUser = mock(User.class);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));

        // when
        stompSSEProducer.publishUserEnter(mockUser, 1L);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }

    @Test
    void 사용자_퇴장_알림이_발행된다() {
        // given
        User mockUser = mock(User.class);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));

        // when
        stompSSEProducer.publishUserLeave(mockUser, 1L);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }

    @Test
    void 코딩_스터디_시작_알림이_발행된다() {
        // given
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));

        // when
        stompSSEProducer.publishStartSpace(1L);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }

    @Test
    void 코딩_스터디_피드백_알림이_발행된다() {
        // given
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));

        // when
        stompSSEProducer.publishFeedback(1L);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }

    @Test
    void 코딩_스터디_종료_알림이_발행된다() {
        // given
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));

        // when
        stompSSEProducer.publishFinish(1L);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }

    @Test
    void 코딩_스터디_테스트케이스_추가_알림이_발행된다() {
        // given
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));

        // when
        stompSSEProducer.publishAddTestCase(mock(TestCaseDto.class), 1L);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }

    @Test
    void 테스트케이스_삭제_알림이_발생한다() {
        // given
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));

        // when
        stompSSEProducer.publishDeleteTestCase(1L, 1L);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }


}