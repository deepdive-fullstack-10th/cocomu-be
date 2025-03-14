package co.kr.cocomu.executor.producer;

import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import co.kr.cocomu.executor.dto.message.SubmissionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompSseProducer {

    private static final String EXECUTION_LOCATION_FORMAT = "/sub/v1/executor/%s";
    private static final String SUBMISSION_LOCATION_FORMAT = "/sub/v1/executor/%s/submission";

    private final SimpMessagingTemplate messagingTemplate;

    public void publishExecutionResult(final EventMessage<ExecutionMessage> result) {
        final String destination = String.format(EXECUTION_LOCATION_FORMAT, result.data().tabId());
        messagingTemplate.convertAndSend(destination, result);
        log.info("ide result message 전송 완료 - {}", result);
    }

    public void publishSubmissionResult(final EventMessage<SubmissionMessage> message) {
        final ExecutionMessage executionMessage = message.data().executionMessage();
        final String destination = String.format(SUBMISSION_LOCATION_FORMAT, executionMessage.tabId());
        messagingTemplate.convertAndSend(destination, message);
        log.error("SUBMISSION RESULT 메시지 전송 - {}", message);
    }

}
