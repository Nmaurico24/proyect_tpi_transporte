package ar.edu.utnfc.backend.ms_logistica.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventoLineaTiempoDTO(
    UUID id,
    String estado,
    String detalle,
    String actor,
    LocalDateTime fecha
) {}