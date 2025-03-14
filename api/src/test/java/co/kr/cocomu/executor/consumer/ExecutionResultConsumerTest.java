package co.kr.cocomu.executor.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.EventType;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import co.kr.cocomu.executor.dto.message.SubmissionMessage;
import co.kr.cocomu.executor.producer.StompSseProducer;
import co.kr.cocomu.executor.service.ExecutorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExecutionResultConsumerTest {

    @Mock private StompSseProducer stompSseProducer;
    @Mock private ExecutorService executorService;

    @InjectMocks private ExecutionResultConsumer executionResultConsumer;

    @Test
    void 코드_실행_결과를_구독_후_실행_결과_이벤트를_발행한다() {
        // given
        // when
        executionResultConsumer.consumeExecutionMessage((EventMessage<ExecutionMessage>) mock(EventMessage.class));

        // then
        verify(stompSseProducer).publishExecutionResult(any(EventMessage.class));
    }

    @Test
    void 코드_제출_결과에서_에러가_발생하면_메시지를_그대로_이벤트로_발행한다() {
        // given
        EventMessage mockEventMessage = mock(EventMessage.class);
        when(mockEventMessage.type()).thenReturn(EventType.COMPILE_ERROR);

        // when
        executionResultConsumer.consumeSubmissionMessage(mockEventMessage);

        // then
        verify(stompSseProducer).publishSubmissionResult(mockEventMessage);
    }

    @Test
    void 코드_제출_결과가_성공일_때_정답이라면_성공_이벤트를_발행한다() {
        // given
        ExecutionMessage executionMessage = new ExecutionMessage(1L, "", 1L, 1L);
        SubmissionMessage submissionMessage = new SubmissionMessage(1L, executionMessage);
        EventMessage<SubmissionMessage> eventMessage = new EventMessage<>(EventType.SUCCESS, submissionMessage);
        when(executorService.checkAnswer(anyLong(), anyString())).thenReturn(true);

        // when
        executionResultConsumer.consumeSubmissionMessage(eventMessage);

        // then
        EventMessage<SubmissionMessage> expected = new EventMessage<>(EventType.CORRECT, submissionMessage);
        verify(stompSseProducer).publishSubmissionResult(expected);
    }

    @Test
    void 코드_제출_결과가_성공일_때_정답이_아니라면_오답_이벤트를_발행한다() {
        // given
        ExecutionMessage executionMessage = new ExecutionMessage(1L, "", 1L, 1L);
        SubmissionMessage submissionMessage = new SubmissionMessage(1L, executionMessage);
        EventMessage<SubmissionMessage> eventMessage = new EventMessage<>(EventType.SUCCESS, submissionMessage);
        when(executorService.checkAnswer(anyLong(), anyString())).thenReturn(false);

        // when
        executionResultConsumer.consumeSubmissionMessage(eventMessage);

        // then
        EventMessage<SubmissionMessage> expected = new EventMessage<>(EventType.WRONG, submissionMessage);
        verify(stompSseProducer).publishSubmissionResult(expected);
    }

}