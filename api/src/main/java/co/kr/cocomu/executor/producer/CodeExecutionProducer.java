package co.kr.cocomu.executor.producer;


import co.kr.cocomu.executor.dto.message.CodeExecutionMessage;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodeExecutionProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public CodeExecutionProducer(
        @Value("${rabbitmq.exchange.name}") final String exchangeName,
        @Value("${rabbitmq.routing.key}") final String routingKey,
        final RabbitTemplate rabbitTemplate
    ) {
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCode(final ExecuteDto dto) {
        final CodeExecutionMessage newMessage = CodeExecutionMessage.createNewMessage(dto);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, newMessage);
        log.info("Code execution 발행 완료 - {}", newMessage);
    }

}
