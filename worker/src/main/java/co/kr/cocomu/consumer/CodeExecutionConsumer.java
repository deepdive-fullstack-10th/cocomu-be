package co.kr.cocomu.consumer;

import co.kr.cocomu.dto.CodeExecutionMessage;
import co.kr.cocomu.dto.EventMessage;
import co.kr.cocomu.dto.ExecutionMessage;
import co.kr.cocomu.service.CodeExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeExecutionConsumer {

    private final CodeExecutionService codeExecutionService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(final CodeExecutionMessage message) {
        log.info("코드 실행 전 메시지 - {}", message.toString());
        final EventMessage<ExecutionMessage> result = codeExecutionService.execute(message);
        log.info("코드 실행 결과 = {}", result);
    }

}
