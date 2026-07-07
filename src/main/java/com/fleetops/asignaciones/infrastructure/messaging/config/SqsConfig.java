package com.fleetops.asignaciones.infrastructure.messaging.config;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.QueueNotFoundStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsConfig {

    // La cola queue_assignations es propiedad de Incidentes (productor).
    // Este servicio solo consume, por lo que no debe intentar crearla:
    // FAIL usa GetQueueUrl/GetQueueAttributes y falla si la cola no existe,
    // en vez de invocar sqs:CreateQueue (acción que este rol IAM no tiene ni necesita).
    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(options -> options.queueNotFoundStrategy(QueueNotFoundStrategy.FAIL))
                .build();
    }
}
