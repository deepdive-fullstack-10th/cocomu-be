package co.kr.cocomu.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeExecutionConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(final CodeExecutionMessage message) {
        log.info("Received code execution request - {}", message.toString());
    }

}
