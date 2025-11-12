package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositoListDTO(
    UUID id,
    String nombre,
    String direccion,
    BigDecimal costoDiario,
    Boolean lsActive
) {}