package ar.edu.utnfc.backend.ms_operaciones.dto.tracking;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TrackingEventoRequest {
    @NotBlank
    private String tipo; // INICIO / FIN
    private BigDecimal lat;
    private BigDecimal lng;
    private String descripcion;
}
