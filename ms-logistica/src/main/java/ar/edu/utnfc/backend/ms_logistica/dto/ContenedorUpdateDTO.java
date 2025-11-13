package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;

public record ContenedorUpdateDTO(
    String etiqueta,
    BigDecimal pesoKg,
    BigDecimal volumenM3,
    String estado,
    Boolean isActive
) {}