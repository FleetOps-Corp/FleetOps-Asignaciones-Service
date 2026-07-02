package com.fleetops.asignaciones.infrastructure.messaging.consumer;

import com.fleetops.asignaciones.application.port.in.ProcesarFallaMecanicaUseCase;
import com.fleetops.asignaciones.domain.event.FallaMecanicaRecibidaEvent;
import com.fleetops.asignaciones.infrastructure.messaging.dto.FallaMecanicaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaIncidentesConsumer {

    private final ProcesarFallaMecanicaUseCase procesarFallaMecanicaUseCase;
    private final ObjectMapper objectMapper;

    /**
     * Driving adapter: escucha fallas mecánicas reportadas por Incidentes.
     * Manual ACK garantiza que el offset solo avanza si el procesamiento fue exitoso.
     */
    @KafkaListener(
            topics = "${asignaciones.kafka.topics.incidentes-falla-mecanica}",
            groupId = "${asignaciones.kafka.consumer.group-id}",
            containerFactory = "kafkaIncidentesListenerContainerFactory"
    )
    public void onFallaMecanica(String mensajeJson, Acknowledgment ack) {
        try {
            FallaMecanicaMessage mensaje = objectMapper.readValue(mensajeJson, FallaMecanicaMessage.class);

            log.info("Incidente recibido. incidente={} tipo={} severidad={} vehiculo={} conductor={}",
                mensaje.incidentId(), mensaje.incidentType(), mensaje.severity(), mensaje.vehicleId(), mensaje.driverId());

            FallaMecanicaRecibidaEvent evento = new FallaMecanicaRecibidaEvent(
                mensaje.incidentId(),
                mensaje.vehicleId(),
                mensaje.description(),
                mensaje.driverId(),
                mensaje.incidentType(),
                mensaje.severity(),
                mensaje.eventDate()
            );
            procesarFallaMecanicaUseCase.procesar(evento);
            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Error procesando incidente recibido: {}", ex.getMessage(), ex);
            // No hacemos ack → Kafka reintentará según la política de reintentos del consumer
        }
    }
}
