package ar.edu.utnfc.backend.ms_logistica.dto;

import ar.edu.utnfc.backend.ms_logistica.models.EstadoSolicitud;
import ar.edu.utnfc.backend.ms_logistica.models.Moneda;

import java.util.UUID;

public record SolicitudSummaryResponse(
        UUID id,
        UUID clienteId,
        EstadoSolicitud estado,
        String origenDireccion,
        String destinoDireccion,
        Double precioEstimado,
        Moneda moneda) {
}
