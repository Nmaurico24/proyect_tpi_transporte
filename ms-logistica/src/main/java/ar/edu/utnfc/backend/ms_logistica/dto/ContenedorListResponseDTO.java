package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ContenedorListResponseDTO(
    UUID id,
    String etiqueta,
    BigDecimal pesoKg,
    BigDecimal volumenM3,
    String estado,
    UUID clienteId
) {}