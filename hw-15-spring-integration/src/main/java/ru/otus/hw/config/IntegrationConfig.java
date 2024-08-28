package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.OrderType;
import ru.otus.hw.services.NotificationService;
import ru.otus.hw.services.OrderProcessingService;
import ru.otus.hw.services.OrderValidationService;
import ru.otus.hw.services.PaymentService;


@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final OrderValidationService orderValidationService;

    private final PaymentService paymentService;

    private final NotificationService notificationService;

    private final OrderProcessingService orderProcessingService;


    @Bean
    public MessageChannelSpec<?, ?> ordersChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> freeOrdersChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> paidOrdersChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> receiptsChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow orderFlow() {
        return IntegrationFlow.from(ordersChannel())
                .handle(orderValidationService, "validate")
                .<Order, OrderType>route(Order::getOrderType,
                        r -> r.subFlowMapping(OrderType.PAID, paidOrderFlow())
                                .subFlowMapping(OrderType.FREE, freeOrderFlow()))
                .get();
    }

    @Bean
    public IntegrationFlow freeOrderFlow() {
        return f -> f
                .channel("freeOrdersChannel")
                .handle(orderProcessingService, "processFreeOrder")
                .handle(notificationService, "sendNotification")
                .transform(paymentService::getReceipt)
                .channel(receiptsChannel());
    }

    @Bean
    public IntegrationFlow paidOrderFlow() {
        return f -> f
                .channel("paidOrdersChannel")
                .handle(paymentService, "processPayment")
                .handle(orderProcessingService, "processPaidOrder")
                .handle(notificationService, "sendNotification")
                .transform(paymentService::getReceipt)
                .channel(receiptsChannel());
    }
}
