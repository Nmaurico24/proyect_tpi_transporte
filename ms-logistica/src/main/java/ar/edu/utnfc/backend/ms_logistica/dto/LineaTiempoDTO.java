package ar.edu.utnfc.backend.ms_logistica.dto;

import java.util.List;
import java.util.UUID;

public record LineaTiempoDTO(
    UUID solicitudId,
    List<EventoLineaTiempoDTO> eventos
) {}