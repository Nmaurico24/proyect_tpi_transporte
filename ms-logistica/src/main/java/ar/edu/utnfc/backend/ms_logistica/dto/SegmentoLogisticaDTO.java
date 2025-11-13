package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SegmentoLogisticaDTO(
    UUID tramoId,
    BigDecimal distanciaKm,
    Integer duracionEstimadaMin,
    UUID camionId,
    UUID depositoOrigenId, 
    UUID depositoDestinoId
) {}
