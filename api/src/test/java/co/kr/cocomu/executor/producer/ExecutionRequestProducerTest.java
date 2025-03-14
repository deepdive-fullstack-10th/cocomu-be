package co.kr.cocomu.executor.producer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import co.kr.cocomu.executor.dto.message.CodeExecutionMessage;
import co.kr.cocomu.executor.dto.message.CodeSubmissionMessage;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.dto.request.SubmitDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ExecutionRequestProducerTest {

    @Mock private RabbitTemplate rabbitTemplate;
    @InjectMocks private ExecutionRequestProducer executionRequestProducer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(executionRequestProducer, "exchangeName", "mockExchange");
        ReflectionTestUtils.setField(executionRequestProducer, "executionSendRoutingKey", "mockKey1");
        ReflectionTestUtils.setField(executionRequestProducer, "submissionSendRoutingKey", "mockKey2");
    }

    @Test
    void 코드_실행_메시지가_발행된다() {
        // given
        // when
        executionRequestProducer.publishExecution(mock(ExecuteDto.class));

        // then
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CodeExecutionMessage.class));
    }

    @Test
    void 코드_제출_메시지가_발행된다() {
        // given
        // when
        executionRequestProducer.publishSubmission(1L, "input", mock(SubmitDto.class));

        // then
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CodeSubmissionMessage.class));
    }

}