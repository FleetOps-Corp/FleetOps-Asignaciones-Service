package com.fleetops.asignaciones.infrastructure.messaging.consumer;

import com.fleetops.asignaciones.application.port.in.ProcesarFallaMecanicaUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("KafkaIncidentesConsumer")
class KafkaIncidentesConsumerTest {

    @Mock
    private ProcesarFallaMecanicaUseCase procesarFallaMecanicaUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private Acknowledgment ack;

    private KafkaIncidentesConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new KafkaIncidentesConsumer(procesarFallaMecanicaUseCase, objectMapper);
    }

    @Test
    @DisplayName("onFallaMecanica: procesa correctamente y hace ACK")
    void onFallaMecanica_procesaCorrectamente_yHaceAck() {

        String mensajeJson = "{" +
                "\"incident_id\":\"" + UUID.randomUUID() + "\"," +
                "\"vehicle_id\":\"" + UUID.randomUUID() + "\"," +
                "\"description\":\"Falla en el motor\"," +
                "\"driver_id\":\"" + UUID.randomUUID() + "\"," +
                "\"incident_type\":\"MECANICO\"," +
                "\"severity\":\"GRAVE\"," +
                "\"event_date\":\"2026-07-02T10:15:00Z\"}";

        consumer.onFallaMecanica(mensajeJson, ack);

        verify(procesarFallaMecanicaUseCase).procesar(any());
        verify(ack).acknowledge();
    }

    @Test
    @DisplayName("onFallaMecanica: si ocurre error NO hace ACK")
    void onFallaMecanica_siHayError_noHaceAck() {

        String mensajeJson = "{invalid-json";

        consumer.onFallaMecanica(mensajeJson, ack);

        verify(ack, never()).acknowledge();
    }

}
