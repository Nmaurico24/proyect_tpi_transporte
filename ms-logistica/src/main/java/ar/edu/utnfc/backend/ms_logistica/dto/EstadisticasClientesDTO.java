package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;

public record EstadisticasClientesDTO(
    Long totalClientes,
    Long clientesActivos,
    Long solicitudesTotales,
    BigDecimal facturacionTotal
) {}