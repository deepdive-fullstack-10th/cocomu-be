package co.kr.cocomu.executor.producer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import co.kr.cocomu.executor.dto.message.CodeExecutionMessage;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CodeExecutionProducerTest {

    @Mock private RabbitTemplate rabbitTemplate;
    private String mockKey = "mockKey";
    private String mockExchange = "mockExchange";

    @InjectMocks private CodeExecutionProducer codeExecutionProducer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(codeExecutionProducer, "exchangeName", mockExchange);
        ReflectionTestUtils.setField(codeExecutionProducer, "routingKey", mockKey);
    }
    @Test
    void 코드_실행_메시지가_발행된다() {
        // given
        ExecuteDto mockDto = new ExecuteDto(1L, "", "", "");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CodeExecutionMessage.class));

        // when
        codeExecutionProducer.publishCode(mockDto);

        // then
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CodeExecutionMessage.class));
    }

}