package com.fleetops.asignaciones.infrastructure.messaging.config;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.QueueNotFoundStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.util.List;

@Configuration
public class SqsConfig {

    // La cola queue_assignations es propiedad de Incidentes (productor).
    // Este servicio solo consume, por lo que no debe intentar crearla ni resolverla por nombre:
    // el rol IAM de este servicio solo tiene sqs:ReceiveMessage/DeleteMessage/ChangeMessageVisibility/
    // GetQueueAttributes (no sqs:GetQueueUrl ni sqs:CreateQueue). Por eso SQS_QUEUE_INCIDENTES_FALLA
    // debe ser la URL completa de la cola (https://sqs.<region>.amazonaws.com/<accountId>/<queueName>):
    // la librería detecta que ya es una URL y omite el GetQueueUrl, usándola directamente.
    // queueAttributeNames fuerza un GetQueueAttributes al arrancar el listener, que sirve como
    // verificación de existencia de la cola (equivalente a lo que antes cubría GetQueueUrl con FAIL).
    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(options -> options
                        .queueNotFoundStrategy(QueueNotFoundStrategy.FAIL)
                        .queueAttributeNames(List.of(QueueAttributeName.QUEUE_ARN)))
                .build();
    }
}
