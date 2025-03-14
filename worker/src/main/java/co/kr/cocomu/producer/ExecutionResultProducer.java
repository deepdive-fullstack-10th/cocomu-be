package co.kr.cocomu.producer;

import co.kr.cocomu.dto.EventMessage;
import co.kr.cocomu.dto.ExecutionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExecutionResultProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String executionSendRoutingKey;
    private final String submissionSendRoutingKey;

    public ExecutionResultProducer(
        @Value("${rabbitmq.exchange.name}") final String exchangeName,
        @Value("${rabbitmq.execution.routing.send}") final String executionSendRoutingKey,
        @Value("${rabbitmq.submission.routing.send}") final String submissionSendRoutingKey,
        final RabbitTemplate rabbitTemplate
    ) {
        this.exchangeName = exchangeName;
        this.executionSendRoutingKey = executionSendRoutingKey;
        this.submissionSendRoutingKey = submissionSendRoutingKey;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishExecution(final EventMessage<ExecutionMessage> message) {
        rabbitTemplate.convertAndSend(exchangeName, executionSendRoutingKey, message);
        log.info("Code execution 발행 완료 - {}", message);
    }

}
