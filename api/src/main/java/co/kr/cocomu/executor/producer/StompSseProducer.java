package co.kr.cocomu.executor.producer;

import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompSseProducer {

    private static final String EXECUTION_LOCATION_FORMAT = "/sub/v1/executor/%s";

    private final SimpMessagingTemplate messagingTemplate;

    public void publishResult(final EventMessage<ExecutionMessage> result) {
        final String destination = String.format(EXECUTION_LOCATION_FORMAT, result.getData().tabId());
        messagingTemplate.convertAndSend(destination, result);
        log.info("ide result message 전송 완료 - {}", result);
    }

}
