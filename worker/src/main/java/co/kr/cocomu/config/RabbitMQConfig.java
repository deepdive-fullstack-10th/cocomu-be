package co.kr.cocomu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
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
    private final String executionRequestQueue;
    // 제출 결과
    private final String submissionReceiveRoutingKey;
    private final String submissionRequestQueue;
    public RabbitMQConfig(
        @Value("${rabbitmq.exchange.name}") final String exchangeName,
        @Value("${rabbitmq.execution.routing.receive}") final String executionReceiveRoutingKey,
        @Value("${rabbitmq.execution.queue.request}") final String executionRequestQueue,
        @Value("${rabbitmq.submission.routing.receive}") final String submissionReceiveRoutingKey,
        @Value("${rabbitmq.submission.queue.request}") final String submissionRequestQueue
    ) {
        this.exchangeName = exchangeName;
        this.executionReceiveRoutingKey = executionReceiveRoutingKey;
        this.executionRequestQueue = executionRequestQueue;
        this.submissionReceiveRoutingKey = submissionReceiveRoutingKey;
        this.submissionRequestQueue = submissionRequestQueue;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }


    @Bean
    public Queue executionRequestQueue() {
        return new Queue(executionRequestQueue);
    }

    @Bean
    public Queue submissionRequestQueue() {
        return new Queue(submissionRequestQueue);
    }

    @Bean
    public Binding executionReceiveBinding() {
        return BindingBuilder.bind(executionRequestQueue())
            .to(directExchange())
            .with(executionReceiveRoutingKey);
    }

    @Bean
    public Binding submissionReceiveBinding() {
        return BindingBuilder.bind(submissionRequestQueue())
            .to(directExchange())
            .with(submissionReceiveRoutingKey);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
        final ConnectionFactory connectionFactory,
        final MessageConverter messageConverter
    ) {
        final SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setMessageConverter(messageConverter);
        containerFactory.setConcurrentConsumers(4);
        containerFactory.setMaxConcurrentConsumers(7);
        containerFactory.setPrefetchCount(1);
        return containerFactory;
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