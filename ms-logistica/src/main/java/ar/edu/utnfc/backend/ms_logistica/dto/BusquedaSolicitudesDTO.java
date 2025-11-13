package ar.edu.utnfc.backend.ms_logistica.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;

public record BusquedaSolicitudesDTO(
    UUID clienteId,
    EstadoSolicitud estado,
    Integer prioridadMin,
    Integer prioridadMax,
    LocalDateTime fechaDesde,
    LocalDateTime fechaHasta,
    String rutaRef
) {}
