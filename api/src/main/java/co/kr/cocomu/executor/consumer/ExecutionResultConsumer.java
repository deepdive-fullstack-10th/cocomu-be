package co.kr.cocomu.executor.consumer;

import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import co.kr.cocomu.executor.producer.StompSseProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExecutionResultConsumer {

    private final StompSseProducer stompSseProducer;

    @RabbitListener(queues = "${rabbitmq.execution.queue.result}")
    public void consumeMessage(final EventMessage<ExecutionMessage> message) {
        stompSseProducer.publishResult(message);
        log.info("코드 실행 결과 전송 완료 = {}", message);
    }

}
