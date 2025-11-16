package ar.edu.utnfc.backend.ms_logistica.dto;

import ar.edu.utnfc.backend.ms_logistica.models.Moneda;

import java.util.UUID;

public record EstimacionResponse(
        UUID id,
        Double distanciaKm,
        Integer duracionMin,
        Double costoTotal,
        Moneda moneda,
        String fuente) {
}
