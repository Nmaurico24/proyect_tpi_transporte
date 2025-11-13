package ar.edu.utnfc.backend.ms_logistica.dto;

import java.util.UUID;

public record ClienteListDTO(
    UUID id,
    String numero,
    String nombre,
    String telefono,
    String email,
    Boolean isActive
) {}