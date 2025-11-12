package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SegmentoCostoDTO(
    UUID tramoId,
    BigDecimal distanciaKm,
    Integer duracionEstimadaMin,
    UUID camionId,
    UUID depositoOrigenId,
    UUID depositoDestinoId
) {}