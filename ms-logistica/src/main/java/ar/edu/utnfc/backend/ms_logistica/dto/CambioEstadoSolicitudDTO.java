package ar.edu.utnfc.backend.ms_logistica.dto;

import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoSolicitudDTO(
    @NotNull EstadoSolicitud estado,
    String detalle,
    String actor
) {}