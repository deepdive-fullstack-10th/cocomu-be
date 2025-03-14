package co.kr.cocomu.executor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    private final String exchangeName;
    // 실행 결과
    private final String executionReceiveRoutingKey;
    private final String executionResultQueue;
    // 제출 결과
    private final String submissionReceiveRoutingKey;
    private final String submissionResultQueue;

    public RabbitMQConfig(
        @Value("${rabbitmq.exchange.name}") final String exchangeName,
        @Value("${rabbitmq.execution.routing.receive}") final String executionReceiveRoutingKey,
        @Value("${rabbitmq.execution.queue.result}") final String executionResultQueue,
        @Value("${rabbitmq.submission.routing.receive}") final String submissionReceiveRoutingKey,
        @Value("${rabbitmq.submission.queue.result}") final String submissionResultQueue

    ) {
        this.exchangeName = exchangeName;
        this.executionReceiveRoutingKey = executionReceiveRoutingKey;
        this.executionResultQueue = executionResultQueue;
        this.submissionReceiveRoutingKey = submissionReceiveRoutingKey;
        this.submissionResultQueue = submissionResultQueue;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue executionResultQueue() {
        return new Queue(executionResultQueue);
    }

    @Bean
    public Queue submissionResultQueue() {
        return new Queue(submissionResultQueue);
    }

    @Bean
    public Binding executionReceiveBinding() {
        return BindingBuilder.bind(executionResultQueue())
            .to(directExchange())
            .with(executionReceiveRoutingKey);
    }

    @Bean
    public Binding submissionReceiveBinding() {
        return BindingBuilder.bind(submissionResultQueue())
            .to(directExchange())
            .with(submissionReceiveRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
        final ConnectionFactory connectionFactory,
        final MessageConverter messageConverter
    ) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter(final ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
