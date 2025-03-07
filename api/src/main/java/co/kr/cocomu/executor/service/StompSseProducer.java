package co.kr.cocomu.executor.service;

import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.EventType;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompSseProducer {

    private static final String EXECUTION_LOCATION_FORMAT = "/sub/executor/%s";

    private final SimpMessagingTemplate messagingTemplate;

    public void publishRunning(final Long codingSpaceTabId) {
        final String destination = String.format(EXECUTION_LOCATION_FORMAT, codingSpaceTabId);
        final ExecutionMessage runningMessage = ExecutionMessage.createRunningMessage(codingSpaceTabId);
        final EventMessage<ExecutionMessage> message = new EventMessage<>(EventType.RUNNING, runningMessage);
        messagingTemplate.convertAndSend(destination, message);
        log.info("ide running message 전송 완료 - {}", message);
    }

}
