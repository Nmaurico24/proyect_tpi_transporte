package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ContenedorListDTO(
    UUID id,
    UUID clienteId,
    String clienteNombre,
    String etiqueta,
    BigDecimal pesoKg,
    BigDecimal volumenM3,
    String estado,
    Boolean isActive
) {}