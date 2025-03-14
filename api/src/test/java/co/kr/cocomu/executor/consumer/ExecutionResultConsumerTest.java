package co.kr.cocomu.executor.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.producer.StompSseProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExecutionResultConsumerTest {

    @Mock
    private StompSseProducer stompSseProducer;

    @InjectMocks
    private ExecutionResultConsumer executionResultConsumer;

    @Test
    void shouldPublishMessageToStomp() {
        // given
        // when
        executionResultConsumer.consumeMessage(mock(EventMessage.class));

        // then
        verify(stompSseProducer).publishResult(any(EventMessage.class));
    }

}