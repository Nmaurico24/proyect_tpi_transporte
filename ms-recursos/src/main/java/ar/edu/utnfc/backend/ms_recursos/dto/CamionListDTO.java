package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CamionListDTO(
    UUID id,
    String dominio,
    String nombre,
    String telefono,
    BigDecimal capPesoKg,
    BigDecimal capVolumeM3,
    Boolean lsActive
) {}