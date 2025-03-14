package co.kr.cocomu.executor.consumer;

import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.EventType;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import co.kr.cocomu.executor.dto.message.SubmissionMessage;
import co.kr.cocomu.executor.producer.StompSseProducer;
import co.kr.cocomu.executor.service.ExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExecutionResultConsumer {

    private final StompSseProducer stompSseProducer;
    private final ExecutorService executorService;

    @RabbitListener(queues = "${rabbitmq.execution.queue.result}")
    public void consumeExecutionMessage(final EventMessage<ExecutionMessage> message) {
        stompSseProducer.publishExecutionResult(message);
        log.info("코드 실행 결과 전송 완료 = {}", message);
    }

    @RabbitListener(queues = "${rabbitmq.submission.queue.result}")
    public void consumeSubmissionMessage(final EventMessage<SubmissionMessage> message) {
        if (message.type() != EventType.SUCCESS) {
            stompSseProducer.publishSubmissionResult(message);
            return;
        }

        final SubmissionMessage submissionMessage = message.data();
        final ExecutionMessage executionMessage = submissionMessage.executionMessage();
        final boolean answer = executorService.checkAnswer(submissionMessage.testCaseId(), executionMessage.output());

        processSuccessResult(answer, submissionMessage);
    }

    private void processSuccessResult(final boolean answer, final SubmissionMessage submissionMessage) {
        if (answer) {
            EventMessage<SubmissionMessage> eventMessage = new EventMessage<>(EventType.CORRECT, submissionMessage);
            stompSseProducer.publishSubmissionResult(eventMessage);
            return;
        }

        EventMessage<SubmissionMessage> eventMessage = new EventMessage<>(EventType.WRONG, submissionMessage);
        stompSseProducer.publishSubmissionResult(eventMessage);
    }

}
