package co.kr.cocomu.executor.producer;


import co.kr.cocomu.executor.dto.message.CodeExecutionMessage;
import co.kr.cocomu.executor.dto.message.CodeSubmissionMessage;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.dto.request.SubmitDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExecutionRequestProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String executionSendRoutingKey;
    private final String submissionSendRoutingKey;

    public ExecutionRequestProducer(
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

    public void publishExecution(final ExecuteDto dto) {
        final CodeExecutionMessage newMessage = CodeExecutionMessage.createNewMessage(dto);
        rabbitTemplate.convertAndSend(exchangeName, executionSendRoutingKey, newMessage);
        log.info("Code execution 발행 완료 - {}", newMessage);
    }

    public void publishSubmission(final Long testCaseId, final String input, final SubmitDto dto) {
        final CodeExecutionMessage codeExecutionMessage = CodeExecutionMessage.of(dto, input);
        final CodeSubmissionMessage codeSubmissionMessage = CodeSubmissionMessage.of(testCaseId, codeExecutionMessage);
        rabbitTemplate.convertAndSend(exchangeName, submissionSendRoutingKey, codeSubmissionMessage);
        log.info("Code submission 발행 완료 - {}", codeSubmissionMessage);
    }

}
