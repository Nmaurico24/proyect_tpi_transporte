package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;

public record SolicitudDTO(
    UUID id,
    UUID clienteId,
    EstadoSolicitud estado,
    Integer prioridad,
    String origenDireccion,
    BigDecimal origenLat,
    BigDecimal origenLng,
    String destinoDireccion,
    BigDecimal destinoLat,
    BigDecimal destinoLng,
    BigDecimal costoEstimado,
    BigDecimal costoFinal,
    BigDecimal distanciaEstimadaKm,
    Integer duracionEstimadaMin,
    Integer duracionRealMin,
    String rutaRef,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}