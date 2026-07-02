package com.fleetops.asignaciones.application.port.out;

import com.fleetops.asignaciones.domain.model.Asignacion;
import java.util.Optional;
import java.util.UUID;

public interface AsignacionRepositoryPort {
    Asignacion guardar(Asignacion asignacion);
    Optional<Asignacion> buscarPorId(UUID id);
    Optional<Asignacion> buscarPorVehiculoId(UUID vehiculoId);
    Optional<Asignacion> buscarPorConductorId(UUID conductorId);
}
