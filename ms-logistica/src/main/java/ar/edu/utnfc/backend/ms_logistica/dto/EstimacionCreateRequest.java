package ar.edu.utnfc.backend.ms_logistica.dto;

import ar.edu.utnfc.backend.ms_logistica.models.Moneda;
import jakarta.validation.constraints.NotNull;

public record EstimacionCreateRequest(
        @NotNull String fuente,
        String payloadJson,
        @NotNull Double distanciaKm,
        @NotNull Integer duracionMin,
        @NotNull Double costoTotal,
        @NotNull Moneda moneda) {
}
