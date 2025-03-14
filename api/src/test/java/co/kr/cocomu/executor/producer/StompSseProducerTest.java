package co.kr.cocomu.executor.producer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.EventType;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import co.kr.cocomu.executor.dto.message.SubmissionMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mock.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@ExtendWith(MockitoExtension.class)
class StompSseProducerTest {

    @Mock(strictness = Strictness.LENIENT) private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks private StompSseProducer stompSseProducer;

    @Test
    void 코드_실행_결과_알림이_발생한다() {
        // given
        EventMessage mockEventMessage = mock(EventMessage.class);
        ExecutionMessage mockExecutionMessage = mock(ExecutionMessage.class);
        when(mockEventMessage.data()).thenReturn(mockExecutionMessage);

        // when
        stompSseProducer.publishExecutionResult(mockEventMessage);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }

    @Test
    void 테스트_케이스_별_정답_결과_알림이_발생한다() {
        // given
        EventMessage mockEventMessage = mock(EventMessage.class);
        SubmissionMessage mockSubmissionMessage = mock(SubmissionMessage.class);
        ExecutionMessage mockExecutionMessage = mock(ExecutionMessage.class);
        when(mockEventMessage.data()).thenReturn(mockSubmissionMessage);
        when(mockSubmissionMessage.executionMessage()).thenReturn(mockExecutionMessage);

        // when
        stompSseProducer.publishSubmissionResult(mockEventMessage);

        // then
        verify(simpMessagingTemplate).convertAndSend(anyString(), any(EventMessage.class));
    }

}