package co.kr.cocomu.producer;

import co.kr.cocomu.dto.result.EventMessage;
import co.kr.cocomu.dto.result.ExecutionMessage;
import co.kr.cocomu.dto.result.SubmissionMessage;
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

    public void publishExecutionResult(final EventMessage<ExecutionMessage> message) {
        rabbitTemplate.convertAndSend(exchangeName, executionSendRoutingKey, message);
        log.info("Code execution 발행 완료 - {}", message);
    }

    public void publishSubmissionResult(final Long testCaseId, final EventMessage<ExecutionMessage> message) {
        final SubmissionMessage submissionMessage = SubmissionMessage.of(testCaseId, message.getData());
        final EventMessage<SubmissionMessage> eventMessage =
            EventMessage.createSubmissionMessage(message.getType(), submissionMessage);

        rabbitTemplate.convertAndSend(exchangeName, submissionSendRoutingKey, eventMessage);
        log.info("Code Submission 결과 발행 완료 - {}", eventMessage);
    }

}
