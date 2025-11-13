package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;

public record SolicitudListDTO(
    UUID id,
    UUID clienteId,
    String clienteNombre,
    EstadoSolicitud estado,
    Integer prioridad,
    String origenDireccion,
    String destinoDireccion,
    BigDecimal costoEstimado,
    String rutaRef,
    LocalDateTime createdAt
) {}