package ar.edu.utnfc.backend.ms_logistica.dto;

import ar.edu.utnfc.backend.ms_logistica.models.Moneda;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SolicitudCreateRequest(
        @NotNull UUID clienteId,
        Integer prioridad,
        @NotNull String origenDireccion,
        @NotNull String destinoDireccion,
        @NotNull Moneda moneda,
        List<UUID> contenedoresIds) {
}
