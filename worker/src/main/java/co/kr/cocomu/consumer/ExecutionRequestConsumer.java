package co.kr.cocomu.consumer;

import co.kr.cocomu.dto.receive.CodeExecutionMessage;
import co.kr.cocomu.dto.receive.CodeSubmissionMessage;
import co.kr.cocomu.dto.result.EventMessage;
import co.kr.cocomu.dto.result.ExecutionMessage;
import co.kr.cocomu.producer.ExecutionResultProducer;
import co.kr.cocomu.service.CodeExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecutionRequestConsumer {

    private final CodeExecutionService codeExecutionService;
    private final ExecutionResultProducer executionResultProducer;

    @RabbitListener(queues = "${rabbitmq.execution.queue.request}")
    public void consumeMessage(final CodeExecutionMessage message) {
        log.info("코드 실행 전 메시지 - {}", message.toString());
        final EventMessage<ExecutionMessage> result = codeExecutionService.execute(message);
        log.info("코드 실행 결과 = {}", result);

        //apiClient.sendResultToMainServer(result);
        executionResultProducer.publishExecutionResult(result);
    }

    @RabbitListener(queues = "${rabbitmq.submission.queue.request}")
    public void consumeMessage(final CodeSubmissionMessage message) {
        log.info("코드 제출 메시지 - {}", message.toString());
        final EventMessage<ExecutionMessage> result = codeExecutionService.execute(message.codeExecutionMessage());
        executionResultProducer.publishSubmissionResult(message.testCaseId(), result);
    }

}
