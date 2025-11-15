package ar.edu.utnfc.backend.ms_logistica.dto;

import ar.edu.utnfc.backend.ms_logistica.models.EstadoSolicitud;
import ar.edu.utnfc.backend.ms_logistica.models.Moneda;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SolicitudDetailResponse(
        UUID id,
        UUID clienteId,
        EstadoSolicitud estado,
        Integer prioridad,
        String origenDireccion,
        String destinoDireccion,
        Double precioEstimado,
        Double precioFinal,
        Double distanciaEstimadaKm,
        Double distanciaRealKm,
        Integer duracionEstimadaMin,
        Integer duracionRealMin,
        Moneda moneda,
        String rutaRef,
        List<UUID> contenedoresIds,
        Instant createdAt) {
}
