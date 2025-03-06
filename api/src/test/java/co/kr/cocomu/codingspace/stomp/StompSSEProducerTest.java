package co.kr.cocomu.codingspace.stomp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import co.kr.cocomu.codingspace.dto.message.EventMessage;
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

    @InjectMocks
    private StompSSEProducer stompSSEProducer;

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
    void 사용자_시작_알림이_발행된다() {
        // given
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));

        // when
        stompSSEProducer.publishStartSpace(1L);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }


}