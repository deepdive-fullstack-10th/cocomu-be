package co.kr.cocomu.codingspace.producer;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}