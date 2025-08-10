package com.docker.mswallet.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange debitExchange() {
        return new TopicExchange(RabbitConstants.WALLET_DEBIT_EXCHANGE);
    }
    @Bean
    public TopicExchange creditExchange() {
        return new TopicExchange(RabbitConstants.WALLET_CREDIT_EXCHANGE);
    }
    @Bean
    public TopicExchange refundExchange() {
        return new TopicExchange(RabbitConstants.WALLET_REFUND_EXCHANGE);
    }

    @Bean
    public Queue debitQueue() {
        return new Queue(RabbitConstants.WALLET_DEBIT_QUEUE, true);
    }
    @Bean
    public Queue debitedQueue() {
        return new Queue(RabbitConstants.WALLET_DEBITED_QUEUE, true);
    }
    @Bean
    public Queue debitFailedQueue() {
        return new Queue(RabbitConstants.WALLET_DEBIT_FAILED_QUEUE, true);
    }
    @Bean
    public Queue creditQueue() {
        return new Queue(RabbitConstants.WALLET_CREDIT_QUEUE, true);
    }
    @Bean
    public Queue creditedQueue() {
        return new Queue(RabbitConstants.WALLET_CREDITED_QUEUE, true);
    }
    @Bean
    public Queue creditFailedQueue() {
        return new Queue(RabbitConstants.WALLET_CREDIT_FAILED_QUEUE, true);
    }
    @Bean
    public Queue refundQueue() {
        return new Queue(RabbitConstants.WALLET_REFUND_QUEUE, true);
    }
    @Bean
    public Queue refundedQueue() {
        return new Queue(RabbitConstants.WALLET_REFUNDED_QUEUE, true);
    }

    @Bean
    public Binding bindDebit() {
        return BindingBuilder
                .bind(debitQueue())
                .to(debitExchange())
                .with(RabbitConstants.WALLET_DEBIT_ROUTING_KEY);
    }

    @Bean public Binding bindDebited() {
        return BindingBuilder
                .bind(debitedQueue())
                .to(debitExchange())
                .with(RabbitConstants.WALLET_DEBITED_ROUTING_KEY);
    }

    @Bean public Binding bindDebitFailed() {
        return BindingBuilder
                .bind(debitFailedQueue())
                .to(debitExchange())
                .with(RabbitConstants.WALLET_DEBIT_FAILED_ROUTING_KEY);
    }

    @Bean public Binding bindCredit() {
        return BindingBuilder
                .bind(creditQueue())
                .to(creditExchange())
                .with(RabbitConstants.WALLET_CREDIT_ROUTING_KEY);
    }

    @Bean public Binding bindCredited() {
        return BindingBuilder
                .bind(creditedQueue())
                .to(creditExchange())
                .with(RabbitConstants.WALLET_CREDITED_ROUTING_KEY);
    }

    @Bean public Binding bindCreditFailed() {
        return BindingBuilder
                .bind(creditFailedQueue())
                .to(creditExchange())
                .with(RabbitConstants.WALLET_CREDIT_FAILED_ROUTING_KEY);
    }

    @Bean public Binding bindRefund() {
        return BindingBuilder
                .bind(refundQueue())
                .to(refundExchange())
                .with(RabbitConstants.WALLET_REFUND_ROUTING_KEY);
    }

    @Bean public Binding bindRefunded() {
        return BindingBuilder
                .bind(refundedQueue())
                .to(refundExchange())
                .with(RabbitConstants.WALLET_REFUNDED_ROUTING_KEY);
    }
}

